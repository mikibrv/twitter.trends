package com.unitbv.twitter.manager.impl;


import com.unitbv.twitter.interfaces.ISlaveNodeController;
import com.unitbv.twitter.manager.AbstractNodeController;
import com.unitbv.twitter.pojo.Node;
import com.unitbv.twitter.pojo.TweetFilter;
import com.unitbv.twitter.processor.TweetFilterProcessor;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import twitter4j.Status;

import javax.annotation.Resource;


/**
 * User: agherasim
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class SlaveNodeController extends AbstractNodeController implements ISlaveNodeController {
    @EndpointInject(uri = "seda:slaveTweetHandler")
    ProducerTemplate producer;

    @Resource(name = "tweetFilterPR")
    TweetFilterProcessor tweetFilterProcessor;

    public SlaveNodeController(Node node) {
        super(node);
    }

    @Override
    public int handleTweet(Status tweet) {
        producer.sendBody(tweet);
        return 0;
    }

    @Override
    public void filterSomeTweets(TweetFilter tweetFilter) {
        if (tweetFilter.getToFilter() != null && tweetFilter.getToFilter().length() > 1) {
            tweetFilterProcessor.setTweetFilter(tweetFilter);
            this.tweetFilter = tweetFilter;
        }
    }

}
