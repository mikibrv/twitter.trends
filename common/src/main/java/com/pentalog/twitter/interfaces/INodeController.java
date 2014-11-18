package com.pentalog.twitter.interfaces;


import com.pentalog.twitter.pojo.Node;

/**
 * User: mcsere
 * Date: 11/14/2014
 * Time: 4:55 PM
 */
public interface INodeController {
    /**
     * Returns difference between pingStart and currentTime;
     *
     * @param pingStart
     * @return
     */
    public long ping(Long pingStart);

    public void addNode(Node node);

    public Node getMaster();
}
