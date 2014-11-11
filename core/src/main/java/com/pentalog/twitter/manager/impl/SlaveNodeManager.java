package com.pentalog.twitter.manager.impl;

import com.pentalog.twitter.manager.AbstractNodeManager;
import com.pentalog.twitter.manager.ISlaveNodeManager;
import com.pentalog.twitter.pojo.Node;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class SlaveNodeManager extends AbstractNodeManager implements ISlaveNodeManager {

    public SlaveNodeManager(Node node) {
        super(node);
    }

    @Override
    public Boolean isMaster() {
        return false;
    }

    @Override
    public void initRoutes() {

    }
}
