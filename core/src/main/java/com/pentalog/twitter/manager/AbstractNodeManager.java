package com.pentalog.twitter.manager;

import com.pentalog.twitter.pojo.Node;

import java.util.HashSet;
import java.util.Set;


/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public abstract class AbstractNodeManager {

    protected Set<Node> clusterNodes = new HashSet<Node>();
    protected Node node;

    public AbstractNodeManager(Node node) {
        this.node = node;
    }

    public Node getMasterNode() {
        for (Node node : clusterNodes) {
            if (node.isMaster()) {
                return node;
            }
        }
        return null;
    }


}
