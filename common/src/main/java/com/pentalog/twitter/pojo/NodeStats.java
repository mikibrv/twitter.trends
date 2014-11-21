package com.pentalog.twitter.pojo;

/**
 * User: mcsere
 * Date: 11/20/2014
 * Time: 11:29 PM
 */
public class NodeStats {

    private int countProcessed;

    public int getCountProcessed() {
        return countProcessed;
    }

    public synchronized void incrementCount() {
        this.countProcessed++;
    }

    public void setCountProcessed(int countProcessed) {
        this.countProcessed = countProcessed;
    }
}
