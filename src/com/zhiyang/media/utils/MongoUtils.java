package com.zhiyang.media.utils;

import java.util.ArrayList;  
import java.util.List;


import com.mongodb.MongoClient;  
import com.mongodb.MongoCredential;  
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;  
 
public class MongoUtils {  
//	private static String ip = "127.0.0.1";
//	private static String ip = "192.168.1.212";
	private static String ip = "106.14.225.229";
	private static int port = 27017;
	private static String user = "acshapp";
	private static String pwd = "U7yhji8u";
    
    public static MongoDatabase getDatabase(String dbname){
//    	return getDatabase(ip, port, dbname);
    	return getDatabase(ip, port, user, pwd, dbname);
    }
    
    public static MongoDatabase getDatabase(String ip,int port,String user,String pwd,String dbname){
    	MongoDatabase db = null;
    	try {  
            //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址  
            //ServerAddress()两个参数分别为 服务器地址 和 端口  
            ServerAddress serverAddress = new ServerAddress(ip,port);  
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();  
            addrs.add(serverAddress);  
              
            //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码  
            MongoCredential credential = MongoCredential.createScramSha1Credential(user, dbname, pwd.toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);  
              
            //通过连接认证获取MongoDB连接  
            MongoClient mongoClient = new MongoClient(addrs,credentials);  
              
            //连接到数据库  
            db = mongoClient.getDatabase(dbname);  
            System.out.println("Connect to database successfully");  
        } catch (Exception e) {  
        }  
    	return db;
    }
    public static MongoDatabase getDatabase(String ip,int port,String dbname){
    	MongoDatabase db = null;
		try {
			MongoClient mongoClient = new MongoClient(ip, port);
			db = mongoClient.getDatabase(dbname);
			System.out.println("Connect to database successfully");  
		} catch (Exception e) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );  
		}
		return db;
    }

} 
