package com.pentalog;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

/**
 * Represents a tweetwords endpoint.
 */
public class tweetwordsEndpoint extends DefaultEndpoint {

    public tweetwordsEndpoint() {
    }

    public tweetwordsEndpoint(String uri, TweetWordsComponent component) {
        super(uri, component);
    }

    public tweetwordsEndpoint(String endpointUri) {
        super(endpointUri);
    }

    public Producer createProducer() throws Exception {
        return new tweetwordsProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new tweetwordsConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }
}
