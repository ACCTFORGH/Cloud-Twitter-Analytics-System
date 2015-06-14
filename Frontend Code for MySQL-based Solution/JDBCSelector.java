//Round robin style load balancing

public class JDBCSelector {

	//Four backend MySQL nodes
	final String url1 = "jdbc:mysql://172.31.56.58:3306/619db?useUnicode=true&characterEncoding=utf-8";
	final String url2 = "jdbc:mysql://172.31.55.28:3306/619db?useUnicode=true&characterEncoding=utf-8";
	final String url3 = "jdbc:mysql://172.31.55.134:3306/619db?useUnicode=true&characterEncoding=utf-8";
	final String url4 = "jdbc:mysql://172.31.57.101:3306/619db?useUnicode=true&characterEncoding=utf-8";
	
	JDBC4 jdbc1 = new JDBC4();
	JDBC4 jdbc2 = new JDBC4();
	JDBC4 jdbc3 = new JDBC4();
	JDBC4 jdbc4 = new JDBC4();
	int count = 0;
	
	public void init(){
		jdbc1.init(url1);
		jdbc2.init(url2);
		jdbc3.init(url3);
		jdbc4.init(url4);
	}
	
	//round robin load balancing
	public String searchSQL(String userid, String tweet_time){
		String ret = "";
		if(count % 4 == 0){
			ret = jdbc1.searchSQL(userid, tweet_time);
		}else if(count % 4 == 1){
			ret = jdbc2.searchSQL(userid, tweet_time);
		}else if(count % 4 == 2){
			ret = jdbc3.searchSQL(userid, tweet_time);
		}else{
			ret = jdbc4.searchSQL(userid, tweet_time);
		}
		count++;
		return ret;
	}
}
