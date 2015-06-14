import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

//JSON String To Java Object
public class StringToDataStructure {
	private static Gson gson = new Gson();
	public String GetJsonToString(String jsonString){
		String str = gson.fromJson(jsonString, String.class);
		return str;
	}
	
	public ArrayList<String> GetJsonToArrayList(String jsonString){
		ArrayList<String> list = 
			gson.fromJson(jsonString, new TypeToken<ArrayList<String>>(){}.getType());
		return list;
	}
	
	public Map<String, String> GetJsonToHashMap(String jsonString){
		HashMap<String,String> hashMap = 
			gson.fromJson(jsonString, new TypeToken<HashMap<String,String>>(){}.getType());
		return hashMap;
		
	}
	
	public Tweet GetJsonToTweet(String jsonString){  
        Tweet tweet = gson.fromJson(jsonString, Tweet.class);  
        return tweet;  
    }
	
	public String TweetToJson(SimpleTweet tweet){
		String str = gson.toJson(tweet, SimpleTweet.class);
		return str;
	}
}
