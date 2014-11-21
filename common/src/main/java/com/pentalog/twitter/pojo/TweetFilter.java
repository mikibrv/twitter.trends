package com.pentalog.twitter.pojo;

import java.io.Serializable;

/**
 * User: mcsere
 * Date: 11/21/2014
 * Time: 8:16 PM
 */
public class TweetFilter implements Serializable {

    private String toFilter;

    private String tweet;

    private String user;

    public String getToFilter() {
        return toFilter;
    }

    public void setToFilter(String toFilter) {
        this.toFilter = toFilter;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TweetFilter{" +
                "toFilter='" + toFilter + '\'' +
                ", tweet='" + tweet + '\'' +
                ", user='" + user + '\'' +
                '}';
    }
}
