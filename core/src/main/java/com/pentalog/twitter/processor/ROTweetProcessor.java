package com.pentalog.twitter.processor;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;
import twitter4j.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mcsere
 * Date: 11/21/2014
 * Time: 3:47 PM
 */
@Component("roTweetProcessor")
public class ROTweetProcessor implements Processor {

    private List<String> toMatch = new ArrayList<String>() {{
        add("BRASOV");
        add("ROMANIA");
    }};

    @EndpointInject(uri = "seda:tweetsAboutRO")
    ProducerTemplate producer;

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn().getBody() instanceof Status) {
            Status tweet = (Status) exchange.getIn().getBody();

            Boolean isFromRO = Boolean.FALSE;
            try {
                isFromRO = containsBrasov(isFromRO, tweet.getText());
                isFromRO = containsBrasov(isFromRO, tweet.getSource());
                isFromRO = tweet.getLang().equalsIgnoreCase("RO");

                isFromRO = containsBrasov(isFromRO, tweet.getPlace().getFullName());
                isFromRO = containsBrasov(isFromRO, tweet.getPlace().getCountry());
                isFromRO = containsBrasov(isFromRO, tweet.getPlace().getName());
                isFromRO = containsBrasov(isFromRO, tweet.getUser().getLocation());
                isFromRO = containsBrasov(isFromRO, tweet.getUser().getName());
            } catch (Exception e) {
                //do nothing;
            }
            if (isFromRO) {
                producer.sendBody(tweet);
            }
        }
    }

    private Boolean containsBrasov(Boolean isAboutBrasov, String toTest) {
        if (isAboutBrasov || toTest == null) {
            return true;
        }
        for (String toMatchStr : toMatch) {
            if (toTest.toLowerCase().contains(toMatchStr.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
