package com.pentalog.twitter;

import com.pentalog.twitter.model.mongoObjects.MongoProperties;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.UriEndpoint;

/**
 * Represents a tweetanalyzer endpoint.
 */
@UriEndpoint(scheme = "tweetanalyzer", consumerClass = TweetAnalyzerConsumer.class)
public class TweetAnalyzerEndpoint extends DefaultEndpoint {

	private MongoProperties mongoProperties;

	public TweetAnalyzerEndpoint() {	}
	public TweetAnalyzerEndpoint(String uri, TweetAnalyzerComponent component, MongoProperties mongoProperties) {

		super(uri, component);
		this.mongoProperties = mongoProperties;
	}

	public TweetAnalyzerEndpoint(String endpointUri) {

		super(endpointUri);
	}

	public Producer createProducer() throws Exception {

		return new TweetAnalyzerProducer(this);
	}

	public Consumer createConsumer(Processor processor) throws Exception {

		return new TweetAnalyzerConsumer(this, processor);
	}

	public boolean isSingleton() {

		return false;
	}

	public MongoProperties getMongoProperties() {

		return mongoProperties;
	}
}
