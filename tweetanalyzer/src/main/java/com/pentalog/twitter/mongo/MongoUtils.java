package com.pentalog.twitter.mongo;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by agherasim on 20/11/2014.
 */

public class MongoUtils {


	public static long trimDateToHours(long date) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(date));
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		return calendar.getTime().getTime();
	}

	public static List<String> splitTweetWords(String tweetText) {

		List<String> words = new ArrayList<String>();
		String[] split = tweetText.split("\\P{L}+");
		for(String word:split) {
			words.add(word.toLowerCase());
		}
		return words;
	}

	public static List<String> getWordsFromTweet(Status tweet) {

		String tweetText = tweet.getText();
		HashtagEntity[] hashtagEntities = tweet.getHashtagEntities();
		List<String> hashTags = new ArrayList<>();
		for (HashtagEntity hashtagEntity : hashtagEntities) {
			hashTags.add(hashtagEntity.getText().toLowerCase());
		}
		List<String> words = MongoUtils.splitTweetWords(tweetText);
		words.addAll(hashTags);
		return words;
	}

	public static boolean containsInList(List<String> list,String object){
		boolean ret=false;
		for(Object element: list){
			if(element instanceof String && object instanceof String) {
				if (((String) element).equalsIgnoreCase((String) object)) {
					ret = true;
					break;
				}
			}
		}

		return ret;
	}

	public static List<Long> addAllHourlyIntervalsIntoList( long minInterval, long maxInterval){
		List<Long> intervals=new ArrayList<>();
		for(long i=trimDateToHours(minInterval);i<=maxInterval;i+=3600000){
			intervals.add(i);
		}
		return intervals;
	}

	public static List<String> addAllHourlyIntervalsIntoListStrings(long minBeginDate, long maxBeginDate) {
		List<String> results=new ArrayList<>();
		List<Long> intervals = addAllHourlyIntervalsIntoList(minBeginDate, maxBeginDate);
		for(long interval:intervals){
			Date date = new Date();
			date.setTime(interval);
			results.add(new SimpleDateFormat("HH:MM d.m.yy").format(date));
		}
		return results;
	}
}
