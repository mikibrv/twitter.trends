package com.pentalog.twitter.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.pentalog.twitter.model.mongoObjects.TweetInfoLight;
import com.pentalog.twitter.model.mongoObjects.Word;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by agherasim on 17/11/2014.
 */
public class MongoStatistics {

	private static MongoQueries mongoQueries;

	private static MongoConnection mongoConnection = MongoConnection.getInstance();

	private static boolean isInit() {

		if (!MongoStatistics.mongoQueries.isInit()) {
			return false;
		}
		return true;
	}

	private static void init() {

		MongoStatistics.mongoQueries.init(MongoStatistics.mongoConnection.getMongoClient());
	}

	public static void processWords(Status tweet) {

		if (!isInit()) {
			init();
		}
		MongoQueries.processWords(tweet);
	}

	public static List<Word> getWordsStatisticsByIntervals(long interval) {

		if (!isInit()) {
			init();
		}
		return MongoQueries.getWords(interval);
	}

	public static DBObject getWordForAllIntervals(String word) {

		if (!isInit()) {
			init();
		}
		return MongoQueries.getWordForAllIntervals(word);
	}

	public static List<String> getTopWordsCountAllTime(int start, int stop) {

		if (!isInit()) {
			init();
		}
		return MongoQueries.getTopWordsCountAllTime(start, stop);
	}

	public static DBObject getGraphData(int start, int stop) {

		if (!isInit()) {
			init();
		}
		DBObject result=new BasicDBObject();
		DBObject mInDate = MongoQueries.getMInDate();
		DBObject maxDate = MongoQueries.getMaxDate();
		result.put("minDate",mInDate);
		result.put("maxDate",maxDate);
		List<String> topWordsCountAllTime = MongoQueries.getTopWordsCountAllTime(start, stop);
		BasicDBList basicDBList = new BasicDBList();
		for (String word : topWordsCountAllTime) {
			basicDBList.add(MongoQueries.getWordForAllIntervals(word));
		}
		result.put("topWordsList",basicDBList);


		return result;
	}
}
