package com.pentalog.twitter.manager;

import com.pentalog.twitter.manager.wrapper.NodeProxy;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeStats;
import org.apache.camel.model.ModelCamelContext;
import org.apache.log4j.Logger;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public abstract class AbstractNodeController {

    protected Logger LOGGER = Logger.getLogger(this.getClass());

    protected List<NodeProxy> clusterNodes = new ArrayList<NodeProxy>();

    protected Node currentNode;

    @Resource(name = "camelContext")
    protected ModelCamelContext camelContext;

    @Resource(name = "nodeStats")
    protected NodeStats nodeStats;

    public AbstractNodeController(Node node) {
        this.currentNode = node;
    }

    public boolean addNode(Node node) {

        try {
            NodeProxy nodeProxy = new NodeProxy(node, camelContext);
            this.clusterNodes.add(nodeProxy);
        } catch (Exception e) {
            LOGGER.error("Unable to add node", e);
            return false;
        }
        return true;

    }

    public Node getMaster() {
        for (NodeProxy nodeProxy : clusterNodes) {
            if (nodeProxy.isMaster()) {
                return nodeProxy.getNode();
            }
        }
        return null;
    }

    public void updateClusterNodes(List<Node> clusterNodes) {
        this.clusterNodes.clear();
        for (Node node : clusterNodes) {
            this.addNode(node);
        }
    }

    public long ping(Long timeStart) {
        LOGGER.warn("PING WAS CALLED");
        return System.currentTimeMillis() - timeStart;
    }

    protected NodeProxy getNodeByUUID(String UUID) {
        for (NodeProxy nodeProxy : clusterNodes) {
            if (nodeProxy.getNode().getUuid().equalsIgnoreCase(UUID)) {
                return nodeProxy;
            }
        }
        return null;
    }

    public NodeStats getNodeStats() {
        return this.nodeStats;
    }
}
