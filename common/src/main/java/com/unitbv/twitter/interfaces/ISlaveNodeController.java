package com.unitbv.twitter.interfaces;

import com.unitbv.twitter.pojo.TweetFilter;
import twitter4j.Status;

/**
 * User: agherasim
 * Date: 11/14/2014
 * Time: 5:05 PM
 */
public interface ISlaveNodeController extends INodeController {

    public int handleTweet(Status tweet);

    public void filterSomeTweets(TweetFilter tweetFilter);

}
