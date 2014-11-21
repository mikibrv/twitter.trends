package com.pentalog.twitter;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link TweetAnalyzerEndpoint}.
 */
public class TweetAnalyzerComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new TweetAnalyzerEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
