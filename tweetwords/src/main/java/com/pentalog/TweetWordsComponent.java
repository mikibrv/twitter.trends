package com.pentalog;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;

/**
 * Represents the component that manages {@link tweetwordsEndpoint}.
 */
public class TweetWordsComponent extends DefaultComponent {

    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new tweetwordsEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
