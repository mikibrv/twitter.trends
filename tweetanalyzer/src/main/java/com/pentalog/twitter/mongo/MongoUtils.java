package com.pentalog.twitter.mongo;

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
		Collections.addAll(words, split);
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
}
