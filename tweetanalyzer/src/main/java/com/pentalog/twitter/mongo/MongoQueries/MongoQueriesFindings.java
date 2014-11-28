package com.pentalog.twitter.mongo.MongoQueries;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.pentalog.twitter.mongo.MongoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by agherasim on 26/11/2014.
 */
public class MongoQueriesFindings extends MongoQueriesAbstract {

	/**
	 * Query:	db.words.aggregate([
	 *	 {$match:{beginDate:{$gte:minBeginDate}}},
	 *	 {$match:{beginDate:{$lte:maxBeginDate}}},
	 *	 {$group:{_id : "$word", count : {$sum : "$count"}}},
	 *	 {$sort:{count:-1}},
	 *	 {$project:{_id:false,word:"$_id",count:true}},
	 *	 {$skip:1},
	 *	 { $limit : 50 }
	 *	 ])
	 * @param minBeginDate
	 * @param maxBeginDateint
	 * @param skipElements
	 * @param limit
	 * @return List of DBObjects : {word:"theWord",count:"int"}
	 */
	public static List<DBObject> findFirstTop_words_intervals(long minBeginDate, long maxBeginDateint, int skipElements,
					int limit) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		List<DBObject> pipeline = new ArrayList<>();
		DBObject match1 = new BasicDBObject();
		match1.put("beginDate", new BasicDBObject("$gte", minBeginDate));
		DBObject match2 = new BasicDBObject();
		match2.put("beginDate", new BasicDBObject("$lte", maxBeginDateint));
		DBObject group = new BasicDBObject();
		group.put("_id", "$word");
		group.put("count", new BasicDBObject("$sum", "$count"));
		DBObject sort = new BasicDBObject();
		sort.put("count", -1);
		DBObject project=new BasicDBObject();
		project.put("_id",false);
		project.put("word","$_id");
		project.put("count",true);
		pipeline.add(new BasicDBObject("$match", match1));
		pipeline.add(new BasicDBObject("$match", match2));
		pipeline.add(new BasicDBObject("$group", group));
		pipeline.add(new BasicDBObject("$sort", sort));
		pipeline.add(new BasicDBObject("$project",project));
		pipeline.add(new BasicDBObject("$skip", skipElements));
		pipeline.add(new BasicDBObject("$limit", limit));
		AggregationOutput aggregate = wordsCollection.aggregate(pipeline);
		Iterable<DBObject> aggregationResult = aggregate.results();
		Iterator<DBObject> aggregationIterator = aggregationResult.iterator();
		List<DBObject> results=new ArrayList<>();
		while(aggregationIterator.hasNext()){
			results.add(aggregationIterator.next());
		}
		return results;
	}

	/**
	 * Query:	db.words.aggregate([
	 * {$match:{beginDate:{$gte:minBeginDate},word:"house"}},
	 * {$match:{beginDate:{$lte:maxBeginDate},word:"house"}},
	 * {$project:{_id:false,beginDate:true,count:true}}])
	 * @param minBeginDate
	 * @param maxBeginDateint
	 * @return List of DBObjects : {beginDate:"theBeginDate",count:"int"}
	 */
	public static List<DBObject> find_word_intervals(String word, long minBeginDate, long maxBeginDateint) {

		DBCollection wordsCollection = getCollectionStatisticsWords();
		List<DBObject> pipeline = new ArrayList<>();
		DBObject match1 = new BasicDBObject();
		match1.put("beginDate", new BasicDBObject("$gte", minBeginDate));
		match1.put("word",word);
		DBObject match2 = new BasicDBObject();
		match2.put("beginDate", new BasicDBObject("$lte", maxBeginDateint));
		match2.put("word", word);
		DBObject project=new BasicDBObject();
		project.put("_id",false);
		project.put("beginDate",true);
		project.put("count",true);
		pipeline.add(new BasicDBObject("$match", match1));
		pipeline.add(new BasicDBObject("$match", match2));
		pipeline.add(new BasicDBObject("$project",project));

		AggregationOutput aggregate = wordsCollection.aggregate(pipeline);
		Iterable<DBObject> aggregationResult = aggregate.results();
		Iterator<DBObject> aggregationIterator = aggregationResult.iterator();
		List<DBObject> results=new ArrayList<>();
		while(aggregationIterator.hasNext()){
			results.add(aggregationIterator.next());
		}
		return results;
	}

}
