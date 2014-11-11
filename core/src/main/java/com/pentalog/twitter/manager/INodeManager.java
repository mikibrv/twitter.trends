package com.pentalog.twitter.manager;

import com.pentalog.twitter.pojo.Node;

import java.io.Serializable;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:47 PM
 */
public interface INodeManager extends Serializable {

    public Boolean isMaster();

    public Node getMasterNode();

    public void initRoutes();
}
