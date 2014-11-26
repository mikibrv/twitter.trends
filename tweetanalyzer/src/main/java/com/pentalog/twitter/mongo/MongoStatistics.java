package com.pentalog.twitter.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.pentalog.twitter.model.mongoObjects.MongoProperties;
import com.pentalog.twitter.model.mongoObjects.Word;
import com.pentalog.twitter.mongo.MongoQueries.MongoOperations;
import twitter4j.Status;

import java.util.List;

/**
 * Created by agherasim on 17/11/2014.
 */
public class MongoStatistics {



	public static MongoStatistics init(MongoProperties mongoProperties) {

		MongoOperations.init(new MongoConnection(mongoProperties).getMongoClient());
		return null;
	}

	public static void processWords(Status tweet) {

		checkInit();
		MongoOperations.processWords(tweet);
	}

	//TODO: COMPLETE THIS FUNCTION!!!!!
	public static DBObject getGraphData(long minBeginDate, long maxBeginDate, int skip, int limit) {


		checkInit();
		DBObject topWordsByCountForIntervals = MongoOperations
						.getTopWordsByCountForIntervals(minBeginDate, maxBeginDate, skip, limit);
		return topWordsByCountForIntervals;
	}


	//	public static List<Word> getWordsStatisticsByIntervals(long interval) {
//
//		checkInit();
//		return MongoOperations.getWords(interval);
//	}
//
//	public static DBObject getWordForAllIntervals(String word) {
//
//		checkInit();
//		return MongoOperations.getWordForAllIntervals(word);
//	}

//	public static List<String> getTopWordsCountAllTime(int start, int stop) {
//
//		checkInit();
//		return MongoQueries.getTopWordsCountAllTime(start, stop);
//	}


	private static void checkInit() throws MongoException{
		if (!isInit()) {
			throw new MongoException("You need to call MongoStatistics.init(mongoProperties) first!!!");
		}
	}
	private static boolean isInit() {

		if (!MongoOperations.isInit()) {
			return false;
		}
		return true;
	}
}
