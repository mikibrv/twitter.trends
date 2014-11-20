package com.pentalog.twitter.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.pentalog.twitter.model.mongoObjects.Country;
import com.pentalog.twitter.model.mongoObjects.TweetInfoLight;
import com.pentalog.twitter.model.mongoObjects.Word;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by agherasim on 18/11/2014.
 */
public class MongoQueries {

	private static MongoClient mongoClient;

	public static boolean isInit() {

		if (mongoClient == null) {
			return false;
		}
		return true;
	}

	public static void init(MongoClient mongoClient) {

		MongoQueries.mongoClient = mongoClient;
	}

	public static List<String> getStopWords() throws MongoException {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		List<String> results = new ArrayList<>();
		DBCollection constantsColl = getCollectionStatisticsConstants();
		DBObject query = new BasicDBObject("stopwords", new BasicDBObject("$exists", 1));
		DBObject stopwords = constantsColl.findOne(query);
		BasicDBList bannedWords = (BasicDBList) stopwords.get("stopwords");
		Object[] bannedWordsObjects = bannedWords.toArray();
		for (Object bannedWord : bannedWordsObjects) {
			results.add(bannedWord.toString());
		}
		return results;
	}

	public static void saveWordsIntoDB(List<Word> words) {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		DBCollection wordsColl = getCollectionStatisticsWords();
		for (Word wordObject : words) {
			DBObject dbObject = getSpecificWord(wordObject.getWordValue(), wordObject.getBeginDateValue());
			if (dbObject != null) {
				Word specificWord = new Word(dbObject);
				for (Country countryObject : wordObject.getCountryListValue()) {
					String countryName = countryObject.getCountryNameValue();
					for (TweetInfoLight tweetInfo : countryObject.getTweetListValue()) {
						specificWord.addTweetInfoToCountry(countryName, tweetInfo);
					}
				}
				wordsColl.update(new BasicDBObject("_id", specificWord.get("_id")), specificWord);
			}
			else {
				wordsColl.insert(wordObject);
			}
		}
	}

	public static Word getSpecificWord(String word, long interval) throws MongoException {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		interval = MongoUtils.trimDateToHours(interval);
		DBCollection wordsColl = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("word", word);
		query.put("beginDate", interval);
		DBObject specificWord = wordsColl.findOne(query);
		Word result = null;
		if (specificWord != null) {
			result = new Word();
			result.putAll(specificWord.toMap());
		}
		return result;
	}

	public static List<Word> getWords(long interval) throws MongoException {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		interval = MongoUtils.trimDateToHours(interval);
		DBCollection wordsColl = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("beginDate", interval);
		DBCursor words = wordsColl.find(query);
		List<Word> result = new ArrayList<>();
		List<DBObject> dbWords = words.toArray();
		for (DBObject word : dbWords) {
			result.add(new Word(word));
		}
		return result;
	}

	private static DBCollection getCollectionStatisticsConstants() {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		String dbName = "statistics";
		DB statisticsDB = mongoClient.getDB(dbName);
		String collectionName = "constants";
		return statisticsDB.getCollection(collectionName);
	}

	private static DBCollection getCollectionStatisticsWords() {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoQueries first!!!");
		}
		String dbName = "statistics";
		DB statisticsDB = mongoClient.getDB(dbName);
		String collectionName = "words";
		return statisticsDB.getCollection(collectionName);
	}
}
