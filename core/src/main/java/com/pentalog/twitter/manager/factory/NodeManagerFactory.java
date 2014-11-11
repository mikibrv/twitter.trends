package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.manager.INodeManager;
import com.pentalog.twitter.manager.impl.MasterNodeManager;
import com.pentalog.twitter.manager.impl.SlaveNodeManager;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeType;
import com.pentalog.twitter.stream.TwitterStreamRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.model.ModelCamelContext;

import javax.annotation.Resource;
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

    @Resource(name = "camelContext")
    private CamelContext camelContext;

    @Resource(name = "twitterStreamRouteBuilder")
    private TwitterStreamRouteBuilder twitterStreamRouteBuilder;


    public INodeManager createNodeManager() throws UnknownHostException {
        NodeType currentNodeType = NodeType.SLAVE;
        if (NodeType.MASTER.toString().equalsIgnoreCase(NODE_TYPE)) {
            currentNodeType = NodeType.MASTER;
        }

        Node node = new Node(UUID.randomUUID().toString(), InetAddress.getLocalHost().getCanonicalHostName());
        node.setType(currentNodeType);
        NodeManagerBuilder builder = new NodeManagerBuilder().setCamelContext((ModelCamelContext) camelContext).setNode(node).setStreamRouteBuilder(twitterStreamRouteBuilder);

        INodeManager nodeManager = null;
        switch (currentNodeType) {
            case SLAVE: {
                nodeManager = builder.buildSlave();
            }
            break;
            case MASTER: {
                nodeManager = builder.buildMaster();
            }
            break;
        }
        nodeManager.initRoutes();
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

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }
}
