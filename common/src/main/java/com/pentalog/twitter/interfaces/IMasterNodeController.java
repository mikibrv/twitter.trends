package com.pentalog.twitter.interfaces;

import com.pentalog.twitter.pojo.TweetFilter;

/**
 * User: mcsere
 * Date: 11/14/2014
 * Time: 5:05 PM
 */
public interface IMasterNodeController extends INodeController {


    public void removeNode(String nodeID);

    public void takeSomeTweets(TweetFilter tweetFilter);

    public void startTweetFiltering(String toFilter);
}
