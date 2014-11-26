package com.pentalog.twitter;

import java.util.Map;

import com.pentalog.twitter.model.mongoObjects.MongoProperties;
import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TweetAnalyzerEndpoint}.
 */
public class TweetAnalyzerComponent extends DefaultComponent {

	private MongoProperties mongoProperties;

	public TweetAnalyzerComponent(MongoProperties mongoProperties){
		this.mongoProperties=mongoProperties;
	}


    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new TweetAnalyzerEndpoint(uri, this,mongoProperties);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
