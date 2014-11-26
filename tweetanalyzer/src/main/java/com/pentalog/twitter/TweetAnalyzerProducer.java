package com.pentalog.twitter;

import com.mongodb.DBObject;
import com.pentalog.twitter.model.mongoObjects.MongoProperties;
import com.pentalog.twitter.mongo.MongoStatistics;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.util.List;

/**
 * The tweetanalyzer producer.
 */
public class TweetAnalyzerProducer extends DefaultProducer {

	private static final Logger LOG = LoggerFactory.getLogger(TweetAnalyzerProducer.class);

	private TweetAnalyzerEndpoint endpoint;

	private static int count = 0;

	public TweetAnalyzerProducer(TweetAnalyzerEndpoint endpoint) {

		super(endpoint);
		this.endpoint = endpoint;
	}

	public void process(Exchange exchange) throws Exception {

		String endpointUri = endpoint.getEndpointUri();
		String command = endpointUri.substring(endpointUri.indexOf("://") + 3);
		MongoProperties mongoProperties = endpoint.getMongoProperties();
		MongoStatistics.init(mongoProperties);
		if (command.startsWith("tweetWords")) {
			Status tweet = (Status) exchange.getIn().getBody();
			MongoStatistics.processWords(tweet);
		}
		else if (command.startsWith("getGraph")) {
			DefaultMessage message = new DefaultMessage();
			try{
				String camelHttpUri = (String) exchange.getIn().getHeaders().get("CamelHttpUri");
				String[] split = camelHttpUri.split("/");

				long minBeginDate= Long.parseLong(split[2]);
				long maxBeginDate= Long.parseLong(split[3]);
				int skip= Integer.parseInt(split[4]);
				int limit= Integer.parseInt(split[5]);
				DBObject graphData = MongoStatistics.getGraphData(minBeginDate, maxBeginDate, skip, limit);
				message.setBody(graphData);
			}catch (Exception e){
				message.setBody("The parameters are incorrect! insert getGraph/\"long minBeginDate\"/\"long maxBeginDate\"/\"int skip\"/\"int limit\"");
			}
			exchange.setOut(message);

		}
	}
}
