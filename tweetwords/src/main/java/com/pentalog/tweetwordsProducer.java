package com.pentalog;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The tweetwords producer.
 */
public class tweetwordsProducer extends DefaultProducer {
    private static final Logger LOG = LoggerFactory.getLogger(tweetwordsProducer.class);
    private tweetwordsEndpoint endpoint;

    public tweetwordsProducer(tweetwordsEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    public void process(Exchange exchange) throws Exception {
        System.out.println(exchange.getIn().getBody());    
    }

}
