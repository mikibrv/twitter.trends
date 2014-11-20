package com.pentalog.twitter.manager.impl;


import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.manager.AbstractNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.manager.wrapper.NodeProxy;
import com.pentalog.twitter.master.TweetBalancer;
import com.pentalog.twitter.pojo.Node;

import java.util.UUID;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class MasterNodeController extends AbstractNodeController implements IMasterNodeController {

    public MasterNodeController(Node node) {
        super(node);
    }

    @Override
    public boolean addNode(Node node) {
        super.addNode(node);
        LOGGER.warn("NODE ADDED:" + node);
        NodeProxy nodeProxy = this.getNodeByUUID(node.getUuid());
        nodeProxy.getSlaveController().ping(System.currentTimeMillis());
        nodeProxy.setCurrentNodeMasterController(this);
        try {
            camelContext.removeRoute(RouteConstants.MASTER_LB_TO_SLAVES);
            camelContext.addRoutes(new TweetBalancer(this.clusterNodes));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void removeNode(String nodeID) {
        NodeProxy toRemove = getNodeByUUID(nodeID);
        if (toRemove != null) {
            toRemove.clearProxy();
            clusterNodes.remove(toRemove);
            try {
                camelContext.removeRoute(RouteConstants.MASTER_LB_TO_SLAVES);
                camelContext.addRoutes(new TweetBalancer(this.clusterNodes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
