package com.unitbv.twitter.model.mongoObjects;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agherasim on 14/11/2014.
 */
public class Country extends BasicDBObject {

	private String countryNameField;

	private String countryNameValue;

	private String countField;

	private int countValue;

	private String tweetListField;

	private List<TweetInfoLight> tweetListValue;

	public Country() {

		super();
		countryNameField = "countryName";
		countField = "count";
		tweetListField = "tweetList";
		tweetListValue = new ArrayList<>();
		countValue = 0;
	}

	public Country(DBObject country) {

		super();
		if (country != null) {
			countryNameField = "countryName";
			countField = "count";
			tweetListField = "tweetList";
			setCountryNameValue((String) country.get("countryName"));
			setCountValue(0);
			tweetListValue = new ArrayList<>();
			BasicDBList tweetList = (BasicDBList) country.get("tweetList");
			for (Object tweetInfoObject : tweetList) {
				DBObject tweetInfo = (DBObject) tweetInfoObject;
				TweetInfoLight tweetInfoLight = new TweetInfoLight(tweetInfo);
				addNewTweetInfo(tweetInfoLight);
			}
		}
	}

	public void incrementCountry() {

		countValue++;
		setCountValue(countValue);
	}

	public void incrementCountry(int ammount) {

		countValue += ammount;
		setCountValue(countValue);
	}

	public void removeTweetInfo(long tweetId) {

		for (TweetInfoLight tweetInfoLight : tweetListValue) {
			if (tweetInfoLight.getTweetIdValue() == tweetId) {
				tweetListValue.remove(tweetInfoLight);
				break;
			}
		}
		setTweetListValue(tweetListValue);
	}

	public void addNewTweetInfo(TweetInfoLight tweetInfoLight) {

		tweetListValue.add(tweetInfoLight);
		setTweetListValue(tweetListValue);
		incrementCountry();
	}

	private void setTweetListValue(List<TweetInfoLight> tweetListValue) {

		this.tweetListValue = tweetListValue;
		this.remove(tweetListField);
		this.append(tweetListField, tweetListValue);
	}

	private void setCountValue(int count) {

		countValue = count;
		this.remove(countField);
		this.append(countField, countValue);
	}

	public void setCountryNameValue(String countryNameValue) {

		this.countryNameValue = countryNameValue;
		this.remove(countryNameField);
		this.append(countryNameField, countryNameValue);
	}

	public String getCountryNameField() {

		return countryNameField;
	}

	public String getCountryNameValue() {

		return countryNameValue;
	}

	public String getCountField() {

		return countField;
	}

	public int getCountValue() {

		return countValue;
	}

	public String getTweetListField() {

		return tweetListField;
	}

	public List<TweetInfoLight> getTweetListValue() {

		return tweetListValue;
	}

	@Override public boolean equals(Object o) {

		if (!o.getClass().equals(getClass())) {
			return false;
		}
		Country country = (Country) o;
		return this.getCountryNameValue().equalsIgnoreCase(country.getCountryNameValue());
	}

	@Override public String toString() {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("{\n");
		stringBuilder.append("\tcountryName: ").append(getCountryNameValue()+"\n");
		stringBuilder.append("\tcount: ").append(getCountValue()+"\n");
		stringBuilder.append("\ttweetList: \n");
		stringBuilder.append("[\n");
		for(TweetInfoLight tweetInfo:getTweetListValue()){
			stringBuilder.append(tweetInfo.toString());
		}
		stringBuilder.append("]\n");
		stringBuilder.append("}\n");
		return stringBuilder.toString();
	}
}
