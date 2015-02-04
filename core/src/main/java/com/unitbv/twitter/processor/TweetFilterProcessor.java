package com.unitbv.twitter.processor;

import com.unitbv.twitter.interfaces.IMasterNodeController;
import com.unitbv.twitter.pojo.TweetFilter;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;
import twitter4j.Status;

/**
 * User: agherasim
 * Date: 11/21/2014
 * Time: 8:19 PM
 */
@Component("tweetFilterPR")
public class TweetFilterProcessor implements Processor {

    /**
     * WARNING, use GET-er and setter when accessing
     */
    private TweetFilter tweetFilter = null;
    private IMasterNodeController masterNodeController;

    public synchronized TweetFilter getTweetFilter() {
        return tweetFilter;
    }

    public synchronized void setTweetFilter(TweetFilter tweetFilter) {
        this.tweetFilter = tweetFilter;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if (getTweetFilter() != null && masterNodeController != null) {
            if (exchange.getIn().getBody() instanceof Status) {
                Status tweet = (Status) exchange.getIn().getBody();
                //do something
                if (tweet.getText().contains(tweetFilter.getToFilter())) {
                    TweetFilter tweetFilterToSend = new TweetFilter();
                    tweetFilterToSend.setToFilter(tweetFilter.getToFilter());
                    tweetFilterToSend.setTweet(tweet.getText());
                    tweetFilterToSend.setUser(tweet.getUser().getName());
                    try {
                        masterNodeController.takeSomeTweets(tweetFilterToSend);
                    } catch (Exception e) {
                        //do nothing;
                    }
                }
            }
        }
    }

    public void setMasterNodeController(IMasterNodeController masterNodeController) {
        this.masterNodeController = masterNodeController;
    }
}
