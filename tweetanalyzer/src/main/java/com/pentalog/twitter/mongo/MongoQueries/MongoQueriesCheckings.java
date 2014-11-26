package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Created by agherasim on 26/11/2014.
 */
public class MongoQueriesCheckings extends MongoQueriesAbstract {

	/**
	 * Query: db.words.find({word:x.word,beginDate:x.beginDate}).limit(1)
	 * @param word
	 * @param beginDate
	 * @return
	 */
	public static boolean check_word_beginDate(String word, long beginDate) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("word", word);
		query.put("beginDate", beginDate);
		DBCursor dbCursor = wordsCollection.find(query).limit(1);
		if (dbCursor != null && dbCursor.hasNext()) {
			return true;
		}
		return false;
	}

	/**
	 * Query: 	db.words.find({word:x.word,beginDate:x.beginDate,"country.countryName":x.country[0].countryName})
	 * 			.limit(1)
	 * @param word
	 * @param beginDate
	 * @param countryName
	 * @return
	 */
	public static boolean check_word_beginDate_countryName(String word, long beginDate, String countryName) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("word", word);
		query.put("beginDate", beginDate);
		query.put("country.countryName", countryName);
		DBCursor dbCursor = wordsCollection.find(query).limit(1);
		if (dbCursor != null && dbCursor.hasNext()) {
			return true;
		}
		return false;
	}

	/**
	 * 	Query: 	db.words.find({word:x.word,beginDate:x.beginDate,"country.countryName":x.country[0].countryName,
	 * 			"country.tweetList.tweetId":x.country[0].tweetList[0].tweetId}).limit(1)
	 * @param word
	 * @param beginDate
	 * @param countryName
	 * @param tweetId
	 * @return
	 */
	public static boolean check_word_beginDate_countryName_tweetId(String word, long beginDate, String countryName,
					long tweetId) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		DBObject query = new BasicDBObject();
		query.put("word", word);
		query.put("beginDate", beginDate);
		query.put("country.countryName", countryName);
		query.put("country.tweetList.tweetId",tweetId);
		DBCursor dbCursor = wordsCollection.find(query).limit(1);
		if (dbCursor != null && dbCursor.hasNext()) {
			return true;
		}
		return false;
	}

	/**
	 * Query: 	db.constants.find({ stopwords:"word" } ).limit(1)
	 * @param word
	 * @return
	 */
	public static boolean check_stopWord(String word){

		DBCollection constantsCollection = getCollectionStatisticsConstants();
		DBObject query = new BasicDBObject();
		query.put("stopwords", word);
		DBCursor dbCursor = constantsCollection.find(query).limit(1);
		if (dbCursor != null && dbCursor.hasNext()) {
			dbCursor.next();
			return true;
		}
		return false;
	}
}
