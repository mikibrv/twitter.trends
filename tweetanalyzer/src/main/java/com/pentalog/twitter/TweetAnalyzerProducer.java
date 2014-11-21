package com.pentalog.twitter;

import com.mongodb.DBObject;
import com.pentalog.twitter.mongo.MongoStatistics;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.component.twitter.producer.Twitter4JProducer;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

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
		String command = endpointUri.substring(endpointUri.indexOf("://")+3);

		switch (command){
			case "tweetWords":{
				Status tweet = (Status) exchange.getIn().getBody();
				MongoStatistics.processWords(tweet);
				break;
			}
			case "getGraph":{
				String camelHttpUri= (String) exchange.getIn().getHeaders().get("CamelHttpUri");
				String[] split = camelHttpUri.split("/");
				int start = new Integer(split[split.length - 2]);
				int stop = new Integer(split[split.length - 1]);
				DBObject graphData = MongoStatistics.getGraphData(start, stop);
				DefaultMessage message = new DefaultMessage();
				message.setBody(graphData);
				exchange.setOut(message);
				break;
			}
			default:{

			}
		}

	}
}
