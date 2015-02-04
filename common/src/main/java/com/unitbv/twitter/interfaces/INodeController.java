package com.unitbv.twitter.interfaces;


import com.unitbv.twitter.pojo.Node;
import com.unitbv.twitter.pojo.NodeStats;

import java.util.List;

/**
 * User: agherasim
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

    public boolean addNode(Node node);

    public Node getMaster();

    public void updateClusterNodes(List<Node> clusterNodes);

    public NodeStats getNodeStats();
}
