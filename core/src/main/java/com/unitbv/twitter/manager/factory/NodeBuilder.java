package com.unitbv.twitter.manager.factory;

import com.unitbv.twitter.manager.enums.RouteConstants;
import com.unitbv.twitter.manager.exceptions.NOInternetException;
import com.unitbv.twitter.pojo.Node;
import com.unitbv.twitter.pojo.NodeStats;
import com.unitbv.twitter.pojo.NodeType;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * User: agherasim
 * Date: 11/17/2014
 * Time: 3:44 PM
 */
@Configuration
public class NodeBuilder {

    Logger LOGGER = Logger.getLogger(this.getClass());

    @Value("${cluster.local.is.master}")
    private Boolean isMaster;
    @Value("${cluster.local.jms.port}")
    private Integer jmsPort;
	@Value("${cluster.local.port}")
	private Integer port;


    private static final String NODE_PREFIX = "Node";

    @Bean(name = "nodeStats")
    public NodeStats buildNodeStats() {
        return new NodeStats();
    }

    @Bean(name = "currentNode")
    public Node buildNode() throws NOInternetException {
        Node node = null;

        NodeType nodeType = isMaster ? NodeType.MASTER : NodeType.SLAVE;
		LOGGER.warn("==============================================================");
        LOGGER.warn("Creating node as " + nodeType + " with port : " + jmsPort);
		LOGGER.warn("==============================================================");
        try {
            node = new Node(NODE_PREFIX + UUID.randomUUID().toString().replaceAll("-", ""), InetAddress.getLocalHost().getHostAddress());
            node.setType(nodeType);
            node.setJMSPort(jmsPort);
			node.setPort(port);
        } catch (UnknownHostException e) {
            throw new NOInternetException(e);
        }

        return node;
    }
}
