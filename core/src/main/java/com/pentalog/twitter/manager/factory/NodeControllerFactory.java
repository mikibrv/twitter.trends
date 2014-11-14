package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.interfaces.INode;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeType;

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
public class NodeControllerFactory {

    private String masterIP;
    private NodeType nodeType;

    @Resource(name = "camelContext")
    private ModelCamelContext camelContext;

    public NodeControllerFactory(String nodeType) {
        if (NodeType.MASTER.toString().equalsIgnoreCase(nodeType)) {
            this.nodeType = NodeType.MASTER;
        } else {
            this.nodeType = NodeType.SLAVE;
        }
    }

    public INode createNodeManager() throws UnknownHostException {

        Node node = new Node(UUID.randomUUID().toString(), InetAddress.getLocalHost().getCanonicalHostName());
        node.setType(nodeType);

        NodeControllerBuilder builder = new NodeControllerBuilder()
                .setCamelContext(camelContext);

        INode nodeController = null;
        switch (nodeType) {
            case SLAVE: {
                nodeController = builder.buildSlave();
            }
            break;
            case MASTER: {
                nodeController = builder.buildMaster();
            }
            break;
        }

        return nodeController;
    }

    public void setMasterIP(String masterIP) {
        this.masterIP = masterIP;
    }

    public void setCamelContext(ModelCamelContext camelContext) {
        this.camelContext = camelContext;
    }

}
