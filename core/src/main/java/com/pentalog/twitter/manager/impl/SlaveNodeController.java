package com.pentalog.twitter.manager.impl;


import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.AbstractNodeController;
import com.pentalog.twitter.pojo.Node;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import twitter4j.Status;



/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class SlaveNodeController extends AbstractNodeController implements ISlaveNodeController {
    @EndpointInject(uri = "seda:slaveTweetHandler")
    ProducerTemplate producer;

    public SlaveNodeController(Node node) {
        super(node);
    }

    @Override
    public int handleTweet(Status tweet) {
        producer.sendBody(tweet);
        return 0;
    }

}
