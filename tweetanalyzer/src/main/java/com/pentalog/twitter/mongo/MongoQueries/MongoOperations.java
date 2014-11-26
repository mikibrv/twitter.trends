package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.pentalog.twitter.model.mongoObjects.AuxWord;
import com.pentalog.twitter.model.mongoObjects.Country;
import com.pentalog.twitter.model.mongoObjects.TweetInfoLight;
import com.pentalog.twitter.model.mongoObjects.Word;
import com.pentalog.twitter.mongo.MongoUtils;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.ArrayList;
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
			tweetInfo.setTweetDateValue(tweet.getCreatedAt().getTime());
			String countryName = "Not mentioned!";
			if (tweet.getPlace() != null && tweet.getPlace().getCountry() != null) {
				countryName = tweet.getPlace().getCountry();
			}
			long beginDate = MongoUtils.trimDateToHours(tweetInfo.getTweetDateValue());
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


	public static DBObject getTopWordsByCountForIntervals(long minBeginDate, long maxBeginDate, int skip,
					int limit) {
		DBObject result=new BasicDBObject();
		List<DBObject> firstTop_words_intervals = MongoQueriesFindings
						.findFirstTop_words_intervals(minBeginDate, maxBeginDate, skip, limit);
		for(DBObject dbObject:firstTop_words_intervals){
			String word= (String) dbObject.get("word");
			result.put(word, MongoQueriesFindings
							.find_word_intervals(word, minBeginDate, maxBeginDate));
		}
		return result;
	}


	//
	//	//TODO: DEPRECATED!!!!
	//	public static List<String> getStopWords() throws MongoException {
	//
	//		List<String> results = new ArrayList<>();
	//		DBCollection constantsColl = getCollectionStatisticsConstants();
	//		DBObject query = new BasicDBObject("stopwords", new BasicDBObject("$exists", 1));
	//		DBObject stopwords = constantsColl.findOne(query);
	//		BasicDBList bannedWords = (BasicDBList) stopwords.get("stopwords");
	//		Object[] bannedWordsObjects = bannedWords.toArray();
	//		for (Object bannedWord : bannedWordsObjects) {
	//			results.add(bannedWord.toString());
	//		}
	//		return results;
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static void saveWordsIntoDB(List<Word> words) {
	//
	//		DBCollection wordsColl = getCollectionStatisticsWords();
	//		for (Word wordObject : words) {
	//			DBObject dbObject = getSpecificWord(wordObject.getWordValue(), wordObject.getBeginDateValue());
	//			if (dbObject != null) {
	//				Word specificWord = new Word(dbObject);
	//				for (Country countryObject : wordObject.getCountryListValue()) {
	//					String countryName = countryObject.getCountryNameValue();
	//					for (TweetInfoLight tweetInfo : countryObject.getTweetListValue()) {
	//						specificWord.addTweetInfoToCountry(countryName, tweetInfo);
	//					}
	//				}
	//				wordsColl.update(new BasicDBObject("_id", specificWord.get("_id")), specificWord);
	//			}
	//			else {
	//				wordsColl.insert(wordObject);
	//			}
	//		}
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static Word getSpecificWord(String word, long interval) throws MongoException {
	//
	//		interval = MongoUtils.trimDateToHours(interval);
	//		DBCollection wordsColl = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		query.put("word", word);
	//		query.put("beginDate", interval);
	//		DBObject specificWord = wordsColl.findOne(query);
	//		Word result = null;
	//		if (specificWord != null) {
	//			result = new Word();
	//			result.putAll(specificWord.toMap());
	//		}
	//		return result;
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static List<Word> getWords(long interval) throws MongoException {
	//
	//		interval = MongoUtils.trimDateToHours(interval);
	//		DBCollection wordsColl = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		query.put("beginDate", interval);
	//		DBCursor words = wordsColl.find(query);
	//		List<Word> result = new ArrayList<>();
	//		List<DBObject> dbWords = words.toArray();
	//		for (DBObject word : dbWords) {
	//			result.add(new Word(word));
	//		}
	//		return result;
	//	}
	//
	//	//TODO: DEPRECATED!!!!
	//	public static DBObject getWordForAllIntervals(String word) {
	//
	//		DBCollection wordsCollection = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		query.put("word", word);
	//		DBObject filter = new BasicDBObject();
	//		filter.put("_id", false);
	//		filter.put("beginDate", true);
	//		filter.put("count", true);
	//		DBObject sortCriteria = new BasicDBObject();
	//		sortCriteria.put("count", -1);
	//		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria);
	//		List<DBObject> dbObjects = results.toArray();
	//		DBObject result = new BasicDBObject();
	//		result.put("word", word);
	//		result.put("intervals", dbObjects);
	//		return result;
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static DBObject getWordForSpecificInterval(String word, long beginDate) {
	//
	//		DBCollection wordsCollection = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		query.put("word", word);
	//		query.put("beginDate", beginDate);
	//		DBObject filter = new BasicDBObject();
	//		filter.put("_id", false);
	//		filter.put("beginDate", true);
	//		filter.put("count", true);
	//		return wordsCollection.findOne(query, filter);
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static int getIndexOfObject(List<AuxWord> auxWords, AuxWord auxWord) {
	//
	//		for (int i = 0; i < auxWords.size(); i++) {
	//			if (auxWords.get(i).word.equalsIgnoreCase(auxWord.word)) {
	//				return i;
	//			}
	//		}
	//		return -1;
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static DBObject getMInDate() {
	//
	//		DBCollection wordsCollection = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		DBObject filter = new BasicDBObject();
	//		filter.put("_id", false);
	//		filter.put("beginDate", true);
	//		DBObject sortCriteria = new BasicDBObject();
	//		sortCriteria.put("beginDate", 1);
	//		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria).limit(1);
	//		return results.toArray().get(0);
	//	}
	//	//TODO: DEPRECATED!!!!
	//	public static DBObject getMaxDate() {
	//
	//		DBCollection wordsCollection = getCollectionStatisticsWords();
	//		DBObject query = new BasicDBObject();
	//		DBObject filter = new BasicDBObject();
	//		filter.put("_id", false);
	//		filter.put("beginDate", true);
	//		DBObject sortCriteria = new BasicDBObject();
	//		sortCriteria.put("beginDate", -1);
	//		DBCursor results = wordsCollection.find(query, filter).sort(sortCriteria).limit(1);
	//		return results.toArray().get(0);
	//	}
}



