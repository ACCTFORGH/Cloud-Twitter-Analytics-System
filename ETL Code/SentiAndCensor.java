import java.util.*;
import java.io.*;

/*This script defines methods for calculating Twitter sentiment score*/

public class SentiAndCensor {
	/** @brief Tweet text parsing result. */
	public class ParseResult{
		// Censored text string.
		public String censoredText;
		// Sentiment score.
		public int score;
		// Constructor.
		public ParseResult(String censoredText, int score){
			this.censoredText = censoredText;
			this.score = score;
		}
	}

	/** @brief Parse a file containing banned words.
	 *  @return Banned word map: <banned word(lowercase), replace string(inner chars only)>
	 */
	public HashMap<String, String> getBannedWords(String file){
		HashMap<String, String> ret = new HashMap<String, String>();
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while((line = br.readLine()) != null){
				line = line.toLowerCase();
				String word = "";
				String replace = "";
				for(int i = 0; i < line.length(); i++){
					char c = line.charAt(i);
					if(c >= 'a' && c <= 'm'){
						c = (char)(c + 13);
					}else if(c >= 'n' && c <= 'z'){
						c = (char)(c - 13);
					}
					word = word + c;
					if(i > 0 && i < line.length() - 1){
						replace = replace + '*';
					}
				}
				ret.put(word, replace);
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ret;
	}
	
	/** @brief Parsea a file containing sentiment scores.
	 *  @return Score map: <word, score>
	 */
	public HashMap<String, Integer> getScoreMap(String file){
		HashMap<String, Integer> ret = new HashMap<String, Integer>();
		try{
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = null;
			while((line = br.readLine()) != null){
				String[] items = line.split("\t");
				ret.put(items[0].toLowerCase(), Integer.parseInt(items[1]));
			}
			br.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return ret;
	}

	/** @brief Parse a uncensored text string given banned words and sentiment score map. */
	public ParseResult parseText(String uncensoredText, HashMap<String, String> bannedWords, 
						HashMap<String, Integer> scoreMap){
		if(uncensoredText == null || uncensoredText.length() == 0){
			return null;
		}
		String censoredText = "";
		int score = 0;
		int start = -1;
		int end = -1;
		int i = 0;
		while(i < uncensoredText.length()){
			char c = uncensoredText.charAt(i);
			if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')){
				if(start == -1){
					// start a new word
					start = i;
					end = i;
				}else{
					// keep parsing
					end = i;
				}
			}else{
				if(end != -1){
					// end of a word
					String word = uncensoredText.substring(start, end + 1);
					String lower = word.toLowerCase();
					// is it banned?
					if(bannedWords.containsKey(lower)){
						word = word.charAt(0) + bannedWords.get(lower) + 
								word.charAt(word.length()-1);
					}
					censoredText = censoredText + word + c;
					// get score
					if(scoreMap.containsKey(lower)){
						score += scoreMap.get(lower);
					}
					// start over
					start = -1;
					end = -1;
				}else{
					// non-alphanumeric
					censoredText = censoredText + c;
				}
			}
			i++;
		}
		// last word
		if(end != -1){
			String word = uncensoredText.substring(start, end + 1);
			String lower = word.toLowerCase();
			if(bannedWords.containsKey(lower)){
				word = word.charAt(0) + bannedWords.get(lower) + word.charAt(word.length()-1);
			}
			censoredText = censoredText + word;
			if(scoreMap.containsKey(lower)){
				score += scoreMap.get(lower);
			}
		}
		ParseResult ret = new ParseResult(censoredText, score);
		return ret;
	}
}
