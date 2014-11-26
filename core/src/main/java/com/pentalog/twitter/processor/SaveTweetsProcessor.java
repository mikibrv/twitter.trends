package com.pentalog.twitter.processor;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;
import twitter4j.Status;

/**
 * User: mcsere
 * Date: 11/21/2014
 * Time: 4:56 PM
 */
@Component("saveTweets")
public class SaveTweetsProcessor implements Processor {

    @EndpointInject(uri = "seda:saveTweets")
    ProducerTemplate producer;

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn().getBody() instanceof Status) {
            Status tweet = (Status) exchange.getIn().getBody();
            producer.sendBody(tweet);
        }
    }
}
