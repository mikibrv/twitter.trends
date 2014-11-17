package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.manager.exceptions.NOInternetException;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * User: mcsere
 * Date: 11/17/2014
 * Time: 3:44 PM
 */
@Configuration
public class NodeBuilder {

    @Resource(name = "currentNodeType")
    NodeType nodeType;
    @Value("${cluster.local.jms.port}")
    private Integer jmsPort;


    @Bean(name = "currentNode")
    public Node buildNode() throws NOInternetException {
        Node node = null;

        try {
            node = new Node(RouteConstants.SLAVE_QUEUE + UUID.randomUUID().toString().replaceAll("-", ""), InetAddress.getLocalHost().getCanonicalHostName());
            node.setType(this.nodeType);
            node.setJMSPort(jmsPort);
        } catch (UnknownHostException e) {
            throw new NOInternetException(e);
        }

        return node;
    }
}
