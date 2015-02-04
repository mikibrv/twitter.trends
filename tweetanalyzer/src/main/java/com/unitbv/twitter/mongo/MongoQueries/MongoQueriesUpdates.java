package com.unitbv.twitter.mongo.MongoQueries;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.unitbv.twitter.model.mongoObjects.TweetInfoLight;

/**
 * Created by agherasim on 26/11/2014.
 */
public class MongoQueriesUpdates extends MongoQueriesAbstract {

	/**
	 * Query: 	db.testWords.update({word:x.word,beginDate:x.beginDate},{$addToSet:{"country":{countryName:"Germany"}}})
	 *
	 * @param word
	 * @param beginDate
	 * @param countryName
	 */
	public static void updateWord_country(String word, long beginDate, String countryName) {

		DBCollection wordCollection = getCollectionStatisticsWords();
		DBObject findQuery = new BasicDBObject();
		findQuery.put("word", word);
		findQuery.put("beginDate", beginDate);
		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("$addToSet", new BasicDBObject("country", new BasicDBObject("countryName", countryName)));
		wordCollection.update(findQuery, updateQuery);
	}

	/**
	 * Query: 	db.testWords.update({word:x.word,beginDate:x.beginDate,"country.countryName":"Germany"},
	 * 			{$addToSet:{"country.$.tweetList":tweetInfo},$inc:{"country.$.count":1}})
	 *
	 * @param word
	 * @param beginDate
	 * @param countryName
	 * @param tweetInfo
	 */
	public static void updateWord_tweetId(String word, long beginDate, String countryName, TweetInfoLight tweetInfo) {

		DBCollection wordCollection = getCollectionStatisticsWords();
		DBObject findQuery = new BasicDBObject();
		findQuery.put("word", word);
		findQuery.put("beginDate", beginDate);
		findQuery.put("country.countryName", countryName);
		DBObject updateQuery = new BasicDBObject();
		updateQuery.put("$addToSet", new BasicDBObject("country.$.tweetList", tweetInfo));
		updateQuery.put("$inc", new BasicDBObject("country.$.count", 1));
		updateQuery.put("$inc", new BasicDBObject("count", 1));
		wordCollection.update(findQuery, updateQuery);
	}
}
