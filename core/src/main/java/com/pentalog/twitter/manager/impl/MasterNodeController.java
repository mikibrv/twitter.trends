package com.pentalog.twitter.manager.impl;


import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.manager.AbstractNodeController;
import com.pentalog.twitter.pojo.Node;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class MasterNodeController extends AbstractNodeController implements IMasterNodeController {

    @Override
    public void addNode(Node node) {
        super.addNode(node);
        LOGGER.warn("NODE ADDED:" + node);
        this.clusterNodes.get(0).getSlaveNodeController().ping(System.currentTimeMillis());
    }
}
