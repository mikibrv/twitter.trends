package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

/**
 * Created by agherasim on 26/11/2014.
 */
public abstract class MongoQueriesAbstract {

	private static MongoClient mongoClient;

	public static MongoClient getMongoClient() {

		return mongoClient;
	}

	public static boolean isInit() {

		if (mongoClient == null) {
			return false;
		}
		return true;
	}

	public static void init(MongoClient mongoClient) {

		MongoQueriesAbstract.mongoClient = mongoClient;
	}

	public static void checkInit() throws MongoException {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
	}

	public static DBCollection getCollectionStatisticsConstants() {

		checkInit();
		String dbName = "statistics";
		DB statisticsDB = mongoClient.getDB(dbName);
		String collectionName = "constants";
		return statisticsDB.getCollection(collectionName);
	}

	public static DBCollection getCollectionStatisticsWords() {

		checkInit();
		String dbName = "statistics";
		DB statisticsDB = mongoClient.getDB(dbName);
		String collectionName = "words";
		return statisticsDB.getCollection(collectionName);
	}
}
