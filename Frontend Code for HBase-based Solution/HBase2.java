import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class HBase2 {
    
	static int MAX_POOL_SIZE = 70;
    static Configuration conf = null;
    HTablePool pool = new HTablePool(conf, MAX_POOL_SIZE);

    
    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "172.31.26.205"); 
        //conf.set("hbase.zookeeper.quorum", "127.0.0.1"); 
    }
    
    String tableName = "q2";
    String familyName = "tweet_profile";
    
    public String searchHBase(String userid, String tweet_time)  {
    	String str = "";
    	
    	try{
    	SimpleDateFormat queryFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		queryFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
	    long time = queryFormat.parse(tweet_time).getTime()/1000L;
	    String rowKey = userid+"+"+time; 
    	HTableInterface table = pool.getTable(tableName);
    	//new HTable(conf, Bytes.toBytes(tableName));
    	
        Get get = new Get(Bytes.toBytes(rowKey));
        get.setMaxVersions(500);
        //System.out.println(userid);
        TreeMap<String,String> tweetId_textScore = new TreeMap<String,String>(); 
        Result result = table.get(get);
        String[] id = new String[500];
        String[] text = new String[500];
        String[] score = new String[500];
        int count=0;
        //System.out.println("size:"+result.list().size());
        int version = result.list().size()/3;
        for (KeyValue kv : result.list()) {
        	if(Bytes.toString(kv.getQualifier()).equals("id")){
        		id[count]=Bytes.toString(kv.getValue());
        	}
        	else if(Bytes.toString(kv.getQualifier()).equals("text")){
        		text[count%version] = Bytes.toString(kv.getValue());
        	}
        	else if(Bytes.toString(kv.getQualifier()).equals("score")){
        		score[count%version] = Bytes.toString(kv.getValue());
        	}
        	count++;
        }    
        for(int i=0; i<version; i++){
        	tweetId_textScore.put(id[i], score[i]+":"+text[i]);
        }
        for(String idstr : tweetId_textScore.keySet()){
        	str = str + idstr + ":" + tweetId_textScore.get(idstr)+"\n"; 
        }
        table.close();
    	}catch(Exception e){
        	System.out.println(e);
        } finally{
        	
        }	
        return str;
    }
}

