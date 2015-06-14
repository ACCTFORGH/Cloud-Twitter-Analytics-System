
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.*;

import snaq.db.*;

public class JDBC4 {
	
	DBPoolDataSource ds = new DBPoolDataSource();
	
	public void init(String url){
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl(url);
		ds.setUser("z2m");
		ds.setPassword("123456");
		ds.setMinPool(36); //4 //20
		ds.setMaxPool(72); //16 //100
		ds.setMaxSize(144); //32 //200
	}
	
	public String searchSQL(String userid, String tweet_time){
		String result = "";
		Connection conn = null;
		try{
			conn = ds.getConnection();
			// SQL statement
			SimpleDateFormat queryFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			queryFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	        long time = queryFormat.parse(tweet_time).getTime()/1000L;
	        String sql = "select * from q2 where uid = "+userid+ " and timestamp = " + time + " order by id";
	        PreparedStatement statement = conn.prepareStatement(sql);
	        ResultSet rs = statement.executeQuery();
	        while(rs.next()) {    
	            // choose column
	            String id = rs.getString("id");
	            String text = rs.getString("text");
	            String score = rs.getString("score");

	            // output
	            //System.out.println(id + "\t" + text); 
	            result = result+id+":"+score+":"+text+"\n";
	        }
	        rs.close();
	        statement.close();
	        conn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
