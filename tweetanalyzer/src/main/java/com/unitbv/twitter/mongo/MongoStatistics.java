package com.unitbv.twitter.mongo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.unitbv.twitter.model.mongoObjects.MongoProperties;
import com.unitbv.twitter.model.mongoObjects.Word;
import com.unitbv.twitter.mongo.MongoQueries.MongoOperations;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agherasim on 17/11/2014.
 */
public class MongoStatistics {



	public static MongoStatistics init(MongoProperties mongoProperties) {

		MongoOperations.init(new MongoConnection(mongoProperties).getMongoClient());
		return null;
	}

	public static int processWords(Status tweet) {

		checkInit();
		return MongoOperations.processWords(tweet);
	}

	public static DBObject getGraphData(long minBeginDate, long maxBeginDate, int skip, int limit) {

		checkInit();
		DBObject result=new BasicDBObject();
		List<DBObject> topWordsByCountForIntervals = MongoOperations
						.getTopWordsByCountForIntervals(minBeginDate, maxBeginDate, skip, limit);
		List<Long> categories=MongoUtils.addAllHourlyIntervalsIntoList(minBeginDate,maxBeginDate);
		List<DBObject> series=new ArrayList<>();

		for(DBObject word:topWordsByCountForIntervals){
			BasicDBObject wrapperWord = new BasicDBObject("name", word.get("word"));
			List<Integer> data=new ArrayList<>();
			List<DBObject> intervals = (List<DBObject>) word.get("intervals");
			for(Long beginDate:categories){
				int count=0;
				for(DBObject interval:intervals){
					long intervalBeginDate = (long) interval.get("beginDate");
					if(beginDate.equals(intervalBeginDate)){
						count = (int) interval.get("count");
						break;
					}
				}
				data.add(count);
			}
			wrapperWord.put("data", data);

			series.add(wrapperWord);
		}
		List<String> categoriesStrings = MongoUtils.addAllHourlyIntervalsIntoListStrings(minBeginDate, maxBeginDate);
		result.put("categories",categoriesStrings);
		result.put("series",series);
		return result;
	}


	private static void checkInit() throws MongoException{
		if (!isInit()) {
			throw new MongoException("You need to call MongoStatistics.init(mongoProperties) first!!!");
		}
	}
	private static boolean isInit() {

		if (!MongoOperations.isInit()) {
			return false;
		}
		return true;
	}
}
