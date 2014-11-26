package com.pentalog.twitter.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.pentalog.twitter.model.mongoObjects.MongoProperties;

import javax.annotation.Resource;
import java.net.UnknownHostException;

/**
 * Created by agherasim on 19/11/2014.
 */
public class MongoConnection {

	private static MongoClient mongoClient;

	public MongoConnection(MongoProperties mongoProperties){
		this.connect(mongoProperties);
	}



	private void connect(MongoProperties mongoProperties){

		try {
			if(mongoClient==null)
				mongoClient = new MongoClient(mongoProperties.getHost(),mongoProperties.getPort());
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public MongoClient getMongoClient() {

		return mongoClient;
	}

}
