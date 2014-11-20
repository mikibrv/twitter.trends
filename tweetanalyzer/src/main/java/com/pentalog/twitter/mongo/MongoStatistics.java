package com.pentalog.twitter.mongo;

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

	public static List<Word> getWordsStatisticsByIntervals(long interval){
		return MongoQueries.getWords(interval);
	}





}
