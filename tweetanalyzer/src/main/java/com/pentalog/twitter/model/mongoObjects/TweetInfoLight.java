package com.pentalog.twitter.model.mongoObjects;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Created by agherasim on 14/11/2014.
 */
public class TweetInfoLight extends BasicDBObject {

	private String tweetIdField;

	private long tweetIdValue;

	private String tweetDateField;

	private long tweetDateValue;

	public TweetInfoLight() {

		super();
		tweetIdField = "tweetId";
		tweetDateField = "tweetDate";
	}

	public TweetInfoLight(DBObject tweetInfo) {

		super();
		if (tweetInfo != null) {
			tweetIdField = "tweetId";
			tweetDateField = "tweetDate";
			setTweetIdValue((long) tweetInfo.get("tweetId"));
			setTweetDateValue((long) tweetInfo.get("tweetDate"));
		}
	}

	public void setTweetDateValue(long tweetDateValue) {

		this.tweetDateValue = tweetDateValue;
		this.remove(tweetDateField);
		this.append(tweetDateField, tweetDateValue);
	}

	public void setTweetIdValue(long tweetIdValue) {

		this.tweetIdValue = tweetIdValue;
		this.remove(tweetIdField);
		this.append(tweetIdField, tweetIdValue);
	}

	public long getTweetDateValue() {

		return tweetDateValue;
	}

	public String getTweetDateField() {

		return tweetDateField;
	}

	public long getTweetIdValue() {

		return tweetIdValue;
	}

	public String getTweetIdField() {

		return tweetIdField;
	}

	@Override public String toString() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{\n\ttweetId: ").append(getTweetIdValue()+"\n");
		stringBuilder.append("\ttweetId: ").append(getTweetDateValue()+"\n}");
		return stringBuilder.toString();
	}
}
