package com.pentalog.twitter;

import com.pentalog.twitter.mongo.MongoStatistics;
import org.apache.camel.Exchange;
import org.apache.camel.component.twitter.producer.Twitter4JProducer;
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

		Status tweet = (Status) exchange.getIn().getBody();
		MongoStatistics.processWords(tweet);
	}
}
