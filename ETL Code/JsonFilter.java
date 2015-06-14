import com.google.gson.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;


/*This script cleans the origin 1TB of Twitter API data and filters out malformed entries*/

public class JsonFilter {
	private  static Gson gson = new Gson();
	public static void main(String[] args) throws Exception{
		Scanner scan = new Scanner(new BufferedReader(new FileReader(args[0])));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[0]+".output")));
		final StringToDataStructure stds = new StringToDataStructure();
		SentiAndCensor t = new SentiAndCensor();
		HashMap<String, String> bannedWords = t.getBannedWords(args[1]);
		HashMap<String, Integer> scoreMap = t.getScoreMap(args[2]);
		
		String line;
		int count=0;
		int filtered_count=0;
		SimpleDateFormat dataformat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
		SimpleDateFormat databaseformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startPoint = dataformat.parse("Sun Apr 20 00:00:00 +0000 2014");
		HashSet<String> ids = new HashSet<String>();
		int duplicates = 0;
		while(scan.hasNext()){
			line = scan.nextLine();
			count++;
			try{


				if(line == null || line.length() < 2){
					continue;
				}
				
				Tweet tweet = stds.GetJsonToTweet(line);
				if(scan.hasNext()) line = scan.nextLine();
				
				//filter malformed lines
				if(tweet.getId().equals("")) continue;
				if(tweet.getUserId().equals("")) continue;
				if(tweet.getText().equals("")) continue;
				if(tweet.getTime().equals("")) continue;
				Date createdAt = dataformat.parse(tweet.getTime());
				if(createdAt.before(startPoint)) continue;
				
				filtered_count++;
				SentiAndCensor.ParseResult ret = t.parseText(tweet.getText(), bannedWords, scoreMap);
				
				writer.write(tweet.getId()); writer.write(0x01);
				writer.write(tweet.getUserId()); writer.write(0x01);
				writer.write(Long.toString(createdAt.getTime()/1000L)); writer.write(0x01);
				writer.write(Integer.toString(ret.score)); writer.write(0x01);
				writer.write(ret.censoredText); writer.write(0x02);

			}catch(Exception e){
				System.err.println(e);
			}			
		}
		writer.flush();
		writer.close();
	}
}
