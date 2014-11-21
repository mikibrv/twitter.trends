package com.pentalog.twitter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import twitter4j.Status;

/**
 * User: mcsere
 * Date: 11/7/2014
 * Time: 6:25 PM
 */
public class TwitterBeanProcessor implements Processor {

    long callsCaptured = 0L;


    @Override
    public void process(Exchange exchange) throws Exception {

        System.out.println("TOTAL " + callsCaptured);
		callsCaptured++;
    }
}
