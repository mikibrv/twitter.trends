package com.pentalog.twitter.manager;

import com.pentalog.twitter.manager.wrapper.NodeProxy;
import com.pentalog.twitter.pojo.Node;
import org.apache.camel.model.ModelCamelContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public abstract class AbstractNodeController {

    protected List<NodeProxy> clusterNodes = new ArrayList<>();

    @Resource(name = "camelContext")
    ModelCamelContext camelContext;

    public long ping(Long timeStart) {
        System.out.println("ceva");
        return System.currentTimeMillis() - timeStart;
    }

    public void addNode(Node node) {

        NodeProxy nodeProxy = new NodeProxy(node);
        try {
            nodeProxy.buildProxy(camelContext);
        } catch (Exception e) {
            e.printStackTrace();
            //failed to add node.
        }

    }

    protected NodeProxy getMasterNode() {
        for (NodeProxy nodeProxy : clusterNodes) {
            if (nodeProxy.getNode().isMaster()) {
                return nodeProxy;
            }
        }
        return null;//kind of dumb isn't it?
    }
}
