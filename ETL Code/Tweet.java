import java.util.Date;
import java.util.Map;


public class Tweet {
	private String created_at;
	private String id;
	private String id_str;
	private String text;
	private String source;
	private boolean truncated;
	private String in_reply_to_status_id;
	private String in_reply_to_status_id_str;
	private String in_reply_to_user_id;
	private String in_reply_to_user_id_str;
	private String in_reply_to_screen_name;
	private Map<String,String> user;
	private Object geo;
	private Object coordinates;      //todo
	private Object place;			//todo
	private Object contributors;     //todo
	private int retweet_count;
	private int favorite_count; 
	private Object entities;
	private boolean favorited;
	private boolean retweeted;
	private String filter_level;
	private String lang;
	
	String getText(){
		return text;
	}
	
	String getTime(){
		return created_at;
	}
	
	String getId(){
		return id_str;
	}
	
	String getUserId(){
		return user.get("id_str");
	}
}


