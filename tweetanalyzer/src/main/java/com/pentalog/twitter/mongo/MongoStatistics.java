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

	public static void init() {

		MongoStatistics.mongoQueries.init(MongoStatistics.mongoConnection.getMongoClient());
	}

	public static void processWords(Status tweet) {

		if (!isInit()) {
			throw new MongoException("Need to run init() into MongoStatistics first!!!");
		}
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
			//			System.out.println("***"+tweetLang + " " + tweetID + " " + tweetDate + " " + country + " " + tweetText+" "+hashTags);
			List<String> words = splitTweetWords(tweetText);
			words.addAll(hashTags);
			TweetInfoLight tweetInfo = new TweetInfoLight();
			tweetInfo.setTweetIdValue(tweetID);
			tweetInfo.setTweetDateValue(tweetDate);
			List<String> stopWords = MongoQueries.getStopWords();
			for (String word : words) {
				if (word.length() < 2) {
					continue;
				}
				if (!stopWords.contains(word)) {
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

	private static List<String> splitTweetWords(String tweetText) {

		List<String> words = new ArrayList<String>();
		String[] split = tweetText.split("\\P{L}+");
		Collections.addAll(words, split);
		return words;
	}


}
