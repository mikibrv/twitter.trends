package com.pentalog.twitter.manager;

import com.pentalog.twitter.pojo.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public abstract class AbstractNodeController {
    protected Node node;

    protected Set<Node> clusterNodes = new HashSet<Node>();

    public Set<Node> getClusterNodes() {
        return this.clusterNodes;
    }


}
