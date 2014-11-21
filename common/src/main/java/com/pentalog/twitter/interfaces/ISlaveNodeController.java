package com.pentalog.twitter.interfaces;

import com.pentalog.twitter.pojo.TweetFilter;
import twitter4j.Status;

/**
 * User: mcsere
 * Date: 11/14/2014
 * Time: 5:05 PM
 */
public interface ISlaveNodeController extends INodeController {

    public int handleTweet(Status tweet);

    public void filterSomeTweets(TweetFilter tweetFilter);

}
