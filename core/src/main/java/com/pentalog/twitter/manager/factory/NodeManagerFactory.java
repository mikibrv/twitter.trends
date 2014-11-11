package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.manager.INodeManager;
import com.pentalog.twitter.manager.impl.MasterNodeManager;
import com.pentalog.twitter.manager.impl.SlaveNodeManager;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeType;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:47 PM
 */
public class NodeManagerFactory {

    private String masterIP;
    private static String NODE_TYPE; //set from main

    public INodeManager createNodeManager() throws UnknownHostException {
        NodeType currentNodeType = NodeType.SLAVE;
        if (NodeType.MASTER.toString().equalsIgnoreCase(NODE_TYPE)) {
            currentNodeType = NodeType.MASTER;
        }

        INodeManager nodeManager = null;

        Node node = new Node(UUID.randomUUID().toString(), InetAddress.getLocalHost().getCanonicalHostName());
        node.setType(currentNodeType);

        switch (currentNodeType) {
            case SLAVE: {
                nodeManager = new SlaveNodeManager(node);
            }
            break;
            case MASTER: {
                nodeManager = new MasterNodeManager(node);
            }
            break;
        }
        return nodeManager;
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = masterIP;
    }

    public static void setNodeType(String[] args) {
        if (args.length > 0) {
            NodeManagerFactory.NODE_TYPE = args[0];
        }
    }
}
