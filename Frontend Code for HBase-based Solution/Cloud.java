import java.util.Date;

import io.undertow.Undertow;
import io.undertow.server.*;
import io.undertow.util.Headers;


public class Cloud {
    public static void main(final String[] args) throws Exception {
    	//System.out.println(Runtime.getRuntime().availableProcessors());
    	final String teamInfo = "z2m,3732-6901-3154,8193-4898-7216,3470-6892-0740\n";
        
    	/*
    	final JDBC4 jdbc = new JDBC4();
        jdbc.init();
        */
    	final HBase2 hbase  = new HBase2();
    	System.out.println(hbase.searchHBase("1000000477","2014-06-02 17:30:21"));
        Undertow server = Undertow.builder()
        		.setIoThreads(25)
                .addHttpListener(80, "0.0.0.0")
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain&charset=utf-8");
                        Date date = new Date();
                        String text = "";
                        
                        //Q1
                        if(exchange.getQueryParameters().get("key")!=null && exchange.getQueryParameters().get("message")!=null){
                        	String key = exchange.getQueryParameters().get("key").getFirst();
                        	String msg = exchange.getQueryParameters().get("message").getFirst();
                        	int Z = Decode.getZ(key);
                        	text = String.format("%tF %tT", date, date) + "\n" + Decode.decode(Z, msg)+"\n";
                        }
                        //Q2
                        else if(exchange.getQueryParameters().get("userid")!=null && exchange.getQueryParameters().get("tweet_time")!=null){
                        	String userid = exchange.getQueryParameters().get("userid").getFirst();
                        	String tweet_time = exchange.getQueryParameters().get("tweet_time").getFirst(); 
                        	//System.out.println(userid+" "+tweet_time);
                        	
                        	//MySQL backend
                        	//text = jdbc.searchSQL(userid, tweet_time);
                        	
                        	//HBase backend
                        	text = hbase.searchHBase(userid,tweet_time);
                        	
                        }
                        //invalid request
                        else{
                        	System.err.println("Invalid request");
                        	text = "hello world\n";
                        }                      
                        exchange.getResponseSender().send(teamInfo+text);
                    }
                }).build();
        server.start();
    }
}