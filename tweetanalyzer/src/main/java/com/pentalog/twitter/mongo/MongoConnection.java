package com.pentalog.twitter.mongo;

import com.mongodb.MongoClient;

import java.net.UnknownHostException;

/**
 * Created by agherasim on 19/11/2014.
 */
public class MongoConnection {

	private static MongoConnection instance;
	private MongoClient mongoClient;

	private MongoConnection(){
		this.connect();
	}

	public static MongoConnection getInstance(){
		if(instance==null){
			instance=new MongoConnection();
		}
		return instance;
	}

	private void connect(){

		try {
			mongoClient = new MongoClient( "79.141.1.187" );
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoClient getMongoClient() {

		return mongoClient;
	}
}
