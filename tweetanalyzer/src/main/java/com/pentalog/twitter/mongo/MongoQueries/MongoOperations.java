package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.pentalog.twitter.model.mongoObjects.TweetInfoLight;
import com.pentalog.twitter.model.mongoObjects.Word;
import com.pentalog.twitter.mongo.MongoUtils;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by agherasim on 18/11/2014.
 */
public class MongoOperations extends MongoQueriesAbstract {

	public static void processWords(Status tweet) {

		if (tweet.getLang().equals("en")) {
			TweetInfoLight tweetInfo = new TweetInfoLight();
			long tweetId = tweet.getId();
			tweetInfo.setTweetIdValue(tweetId);
			tweetInfo.setTweetDateValue(new Date(tweet.getCreatedAt().getTime()));
			String countryName = "Not mentioned!";
			if (tweet.getPlace() != null && tweet.getPlace().getCountry() != null) {
				countryName = tweet.getPlace().getCountry();
			}
			long beginDate = MongoUtils.trimDateToHours(tweetInfo.getTweetDateValue().getTime());
			List<String> wordsFromTweet = MongoUtils.getWordsFromTweet(tweet);
			registerWords(wordsFromTweet, beginDate, countryName, tweetInfo);
		}
	}

	private static void registerWords(List<String> wordsFromTweet, long beginDate, String countryName,
					TweetInfoLight tweetInfo) {

		for (String word : wordsFromTweet) {
			if(!MongoQueriesCheckings.check_stopWord(word) && word.length()>2) {
				long tweetId = tweetInfo.getTweetIdValue();
				if (!MongoQueriesCheckings
								.check_word_beginDate_countryName_tweetId(word, beginDate, countryName, tweetId)) {
					if (!MongoQueriesCheckings.check_word_beginDate_countryName(word, beginDate, countryName)) {
						if (!MongoQueriesCheckings.check_word_beginDate(word, beginDate)) {
							Word wordDBObject = new Word();
							wordDBObject.setWordValue(word);
							wordDBObject.setBeginDateValue(beginDate);
							wordDBObject.addTweetInfoToCountry(countryName, tweetInfo);
							MongoQueriesInserts.insertWord(wordDBObject);
							break;
						}
						MongoQueriesUpdates.updateWord_country(word, beginDate, countryName);
					}
					MongoQueriesUpdates.updateWord_tweetId(word, beginDate, countryName, tweetInfo);
				}
			}
		}
	}

	/**
	 *
	 * @param minBeginDate
	 * @param maxBeginDate
	 * @param skip
	 * @param limit
	 * @return List of DBObjects
	 * DBObject:
	 * {
	 *     word:"some word"
	 *     intervals:
	 *		[
	 *     		{
	 *     			beginDate:"theBeginDate",
	 *     			count:"int"
	 *     		},
	 *     		{...}
	 *     	]
	 * }
	 */
	public static List<DBObject> getTopWordsByCountForIntervals(long minBeginDate, long maxBeginDate, int skip,
					int limit) {
		List<DBObject> result=new ArrayList<>();
		List<DBObject> firstTop_words_intervals = MongoQueriesFindings
						.findFirstTop_words_intervals(minBeginDate, maxBeginDate, skip, limit);
		for(DBObject dbObject:firstTop_words_intervals){
			String word= (String) dbObject.get("word");
			BasicDBObject wrapper = new BasicDBObject("word", word);
			wrapper.put("intervals",MongoQueriesFindings.find_word_intervals(word, minBeginDate, maxBeginDate));
			result.add(wrapper);
		}
		return result;
	}
}



