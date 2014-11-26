package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.DBCollection;
import com.pentalog.twitter.model.mongoObjects.Word;

/**
 * Created by agherasim on 26/11/2014.
 */
public class MongoQueriesInserts extends MongoQueriesAbstract {

	/**
	 * Query:	db.testWords.insert(x)
	 * @param word
	 */
	public static void insertWord(Word word){

		DBCollection wordCollection = getCollectionStatisticsWords();
		wordCollection.insert(word);
	}
}
