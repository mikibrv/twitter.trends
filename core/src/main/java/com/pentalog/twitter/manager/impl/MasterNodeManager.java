package com.pentalog.twitter.manager.impl;

import com.pentalog.twitter.manager.AbstractNodeManager;
import com.pentalog.twitter.manager.IMasterNodeManager;
import com.pentalog.twitter.pojo.Node;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class MasterNodeManager extends AbstractNodeManager implements IMasterNodeManager {
    public MasterNodeManager(Node node) {
        super(node);
        clusterNodes.add(node);
    }

    @Override
    public Boolean isMaster() {
        return true;
    }

    @Override
    public void initRoutes() {

    }
}
