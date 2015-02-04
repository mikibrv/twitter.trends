package com.unitbv.twitter.interfaces;

import com.unitbv.twitter.pojo.TweetFilter;

/**
 * User: agherasim
 * Date: 11/14/2014
 * Time: 5:05 PM
 */
public interface IMasterNodeController extends INodeController {


    public void removeNode(String nodeID);

    public void takeSomeTweets(TweetFilter tweetFilter);

    public void startTweetFiltering(String toFilter);
}
