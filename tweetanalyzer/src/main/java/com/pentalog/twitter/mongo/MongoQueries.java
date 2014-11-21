package com.pentalog.twitter.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.pentalog.twitter.model.mongoObjects.AuxObject;
import com.pentalog.twitter.model.mongoObjects.Country;
import com.pentalog.twitter.model.mongoObjects.TweetInfoLight;
import com.pentalog.twitter.model.mongoObjects.Word;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

	public static void processWords(Status tweet) {

		Message message = new DefaultMessage();
		String tweetLang = tweet.getLang();
		long tweetID = tweet.getId();
		long tweetDate = tweet.getCreatedAt().getTime();
		String tweetText = tweet.getText();
		String country = "Not mentioned!";
		if (tweet.getPlace() != null && tweet.getPlace().getCountry() != null) {
			country = tweet.getPlace().getCountry();
		}
		HashtagEntity[] hashtagEntities = tweet.getHashtagEntities();
		List<String> hashTags = new ArrayList<>();
		for (HashtagEntity hashtagEntity : hashtagEntities) {
			hashTags.add(hashtagEntity.getText());
		}
		List<Word> wordObjects = new ArrayList<>();
		if (tweetLang.equals("en")) {
			List<String> words = MongoUtils.splitTweetWords(tweetText);
			words.addAll(hashTags);
			TweetInfoLight tweetInfo = new TweetInfoLight();
			tweetInfo.setTweetIdValue(tweetID);
			tweetInfo.setTweetDateValue(tweetDate);
			List<String> stopWords = MongoQueries.getStopWords();
			for (String word : words) {
				if (word.length() < 3) {
					continue;
				}
				if (!MongoUtils.containsInList(stopWords, word)) {
					Word wordObject = new Word();
					wordObject.setWordValue(word);
					wordObject.setBeginDateValue(MongoUtils.trimDateToHours(tweetDate));
					wordObject.addTweetInfoToCountry(country, tweetInfo);
					wordObjects.add(wordObject);
				}
			}
		}
		MongoQueries.saveWordsIntoDB(wordObjects);
	}

	public static DBObject getWordForAllIntervals(String word) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("word", word);
		DBObject filter = new BasicDBObject();
		filter.put("_id", false);
		filter.put("beginDate", true);
		filter.put("count", true);
		DBObject sortCriteria = new BasicDBObject();
		sortCriteria.put("count", -1);
		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria);
		DBObject result = new BasicDBObject();
		result.put("word", word);
		result.put("intervals", results.toArray());
		return result;
	}

	public static List<String> getTopWordsCountAllTime(int start, int stop) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		DBObject filter = new BasicDBObject();
		filter.put("_id", false);
		filter.put("word", true);
		filter.put("count", true);
		DBObject sortCriteria = new BasicDBObject();
		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria);
		List<DBObject> dbObjects = results.toArray();
		List<AuxObject> auxObjects = new ArrayList<>();
		for (DBObject dbObject : dbObjects) {
			AuxObject auxObject = new AuxObject();
			auxObject.word = (String) dbObject.get("word");
			auxObject.count = (Integer) dbObject.get("count");
			int indexOfObject = getIndexOfObject(auxObjects, auxObject);
			if(indexOfObject==-1) {
				auxObjects.add(auxObject);
			}else{
				auxObject.count+=auxObjects.get(indexOfObject).count;
				auxObjects.set(indexOfObject, auxObject);
			}
		}
		Comparator<? super AuxObject> comparator = new Comparator<AuxObject>() {

			@Override public int compare(AuxObject o1, AuxObject o2) {

				return o1.count.compareTo(o2.count) * (-1);
			}
		};
		Collections.sort(auxObjects, comparator);
		List<String> result = new ArrayList<>();
		for (int i = start; (i < stop && i < auxObjects.size()); i++) {
			result.add(auxObjects.get(i).word);
		}
		return result;
	}

	public static int getIndexOfObject(List<AuxObject> auxObjects, AuxObject auxObject){
		for(int i=0;i<auxObjects.size();i++){
			if(auxObjects.get(i).word.equalsIgnoreCase(auxObject.word))
			{
				return i;
			}
		}
		return -1;
	}

	public static DBObject getMInDate() {
		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		DBObject filter = new BasicDBObject();
		filter.put("_id", false);
		filter.put("beginDate", true);
		DBObject sortCriteria = new BasicDBObject();
		sortCriteria.put("beginDate",1);
		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria).limit(1);
		return results.toArray().get(0);
	}
	public static DBObject getMaxDate() {
		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		DBObject filter = new BasicDBObject();
		filter.put("_id", false);
		filter.put("beginDate", true);
		DBObject sortCriteria = new BasicDBObject();
		sortCriteria.put("beginDate",-1);
		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria).limit(1);
		return results.toArray().get(0);
	}
}



