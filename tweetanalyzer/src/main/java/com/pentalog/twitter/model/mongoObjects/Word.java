package com.pentalog.twitter.model.mongoObjects;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agherasim on 14/11/2014.
 */
public class Word extends BasicDBObject {


	private ObjectId _id;

	private String wordField;

	private String wordValue;

	private String beginDateField;

	private long beginDateValue;

	private String countField;

	private int countValue;

	private String countryListField;

	private List<Country> countryListValue;

	public Word() {

		super();
		wordField = "word";
		beginDateField = "beginDate";
		countField="count";
		countryListField = "country";
		countryListValue = new ArrayList<>();
		countValue = 0;
	}

	public Word(DBObject word) {

		super();
		if(word != null) {
			wordField = "word";
			beginDateField = "beginDate";
			countField = "count";
			countryListField = "country";
			set_id((ObjectId) word.get("_id"));
			setWordValue((String) word.get("word"));
			setBeginDateValue((long) word.get("beginDate"));
			setCountValue( (int) word.get("count"));
			countryListValue = new ArrayList<>();
			BasicDBList countryList = (BasicDBList) word.get("country");
			for (Object countryObject : countryList) {
				DBObject countryDBObject = (DBObject) countryObject;
				Country country = new Country(countryDBObject);
				addCountry(country);
			}
		}
	}

	private void incrementWord() {

		countValue++;
		setCountValue(countValue);

	}

	private void incrementWord(int ammount) {

		countValue += ammount;
		setCountValue(countValue);

	}

	private void setCountValue(int countValue){
		this.countValue=countValue;
		remove(countField);
		append(countField,countValue);
	}

	public void addTweetInfoToCountry(String countryNname, TweetInfoLight tweetInfo) {

		boolean tweetRegistered=false;
		Country auxCountry = new Country();
		auxCountry.setCountryNameValue(countryNname);
		for(Country country: countryListValue){
			if(country.getCountryNameValue().equalsIgnoreCase(countryNname)){
				auxCountry=country;
				List<TweetInfoLight> tweetListValue = auxCountry.getTweetListValue();
				for(TweetInfoLight tweetInfoLight: tweetListValue){
					if(tweetInfoLight.getTweetIdValue()==tweetInfo.getTweetIdValue()){
						tweetRegistered=true;
						break;
					}
				}

			}
		}
		if(tweetRegistered!=true) {
			incrementWord();
			auxCountry.addNewTweetInfo(tweetInfo);
			removeCountry(countryNname);
			addCountry(auxCountry);
		}

	}


	private void removeCountry(String countryName) {

		for (Country country : countryListValue) {
			if (country.getCountryNameValue().equalsIgnoreCase(countryName)) {
				countryListValue.remove(country);
				break;
			}
		}
		setCountryListValue(countryListValue);
	}

	private void addCountry(Country country) {

		countryListValue.add(country);
		setCountryListValue(countryListValue);
	}

	private void setCountryListValue(List<Country> countryListValue) {

		this.countryListValue = countryListValue;
		this.remove(countryListField);
		this.append(countryListField, countryListValue);
	}

	public void setBeginDateValue(long beginDateValue) {

		this.beginDateValue = beginDateValue;
		this.remove(beginDateField);
		this.append(beginDateField, beginDateValue);
	}

	public void setWordValue(String wordValue) {

		this.wordValue = wordValue;
		this.remove(wordField);
		this.append(wordField, wordValue);
	}

	public List<Country> getCountryListValue() {

		return countryListValue;
	}

	public String getCountryListField() {

		return countryListField;
	}

	public long getBeginDateValue() {

		return beginDateValue;
	}

	public String getBeginDateField() {

		return beginDateField;
	}

	public String getWordValue() {

		return wordValue;
	}

	public String getWordField() {

		return wordField;
	}

	public int getCountValue() {

		return countValue;
	}

	public ObjectId get_id() {

		return _id;
	}

	public void set_id(ObjectId _id) {

		this._id = _id;
		remove("_id");
		append("_id",_id);

	}

	@Override public String toString() {

		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{\n");
		stringBuilder.append("_id: ").append(get_id()+"\n");
		stringBuilder.append("word: ").append(getWordValue()+"\n");
		stringBuilder.append("beginDate: ").append(getBeginDateValue()+"\n");
		stringBuilder.append("count: ").append(getCountValue()+"\n");
		stringBuilder.append("country:\n");
		stringBuilder.append("{\n");
		for(Country country:getCountryListValue()){
			stringBuilder.append(country.toString());
		}
		stringBuilder.append("}\n");
		stringBuilder.append("}\n");

		return stringBuilder.toString();
	}
}
