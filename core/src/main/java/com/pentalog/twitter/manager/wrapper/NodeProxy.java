package com.pentalog.twitter.manager.wrapper;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.INodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.manager.exceptions.BadMessageException;
import com.pentalog.twitter.manager.exceptions.SlaveCrashedException;
import com.pentalog.twitter.pojo.Node;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.log4j.Logger;
import twitter4j.Status;

/**
 * User: mcsere
 * Date: 11/17/2014
 * Time: 4:53 PM
 */
public class NodeProxy implements Processor {

    Logger LOGGER = Logger.getLogger(this.getClass());
    private Node node;
    private INodeController nodeController = null;
    private ModelCamelContext camelContext;


    public NodeProxy(Node node, ModelCamelContext camelContext) throws Exception {
        this.node = node;
        this.camelContext = camelContext;
        this.buildProxyOverNode();
    }

    public IMasterNodeController getMasterController() {
        if (this.isMaster()) {
            return (IMasterNodeController) this.nodeController;
        }
        return null;
    }

    public ISlaveNodeController getSlaveController() {
        if (this.isSlave()) {
            return (ISlaveNodeController) this.nodeController;
        }
        return null;
    }

    public void buildProxyOverNode() throws Exception {
        ActiveMQConfiguration activeMQConfiguration = new ActiveMQConfiguration();
        activeMQConfiguration.setBrokerURL(getPathTOProxyJMS(node.getIP(), node.getJMSPort()));
        activeMQConfiguration.setAutoStartup(true);
        camelContext.addComponent(node.getUuid(), new ActiveMQComponent(activeMQConfiguration));
        LOGGER.warn("Added ActiveMQComponent to: " + getPathTOProxyJMS(node.getIP(), node.getJMSPort()));
        //build the proxies depending on nodeType;
        if (this.node.isMaster()) {
            this.nodeController = new ProxyBuilder(camelContext).
                    endpoint(node.getUuid() + ":queue:" + RouteConstants.MASTER_QUEUE).
                    build(IMasterNodeController.class);
            LOGGER.warn("Created MasterProxy: " + node.getUuid());
        } else {
            this.nodeController = new ProxyBuilder(camelContext).
                    endpoint(node.getUuid() + ":queue:" + RouteConstants.SLAVE_QUEUE).
                    build(ISlaveNodeController.class);
            LOGGER.warn("Created SlaveProxy" + node.getUuid());
        }
    }

    public void clearProxy() {
        camelContext.removeComponent(node.getUuid());
        this.nodeController = null;
    }

    /**
     * returns the JMS Path
     *
     * @param ip
     * @param port
     * @return
     */
    private String getPathTOProxyJMS(String ip, Integer port) {
        return RouteConstants.JMS_PROTOCOL + "://" + ip + ":" + port;
    }


    public Boolean isMaster() {
        return node.isMaster();
    }

    public Boolean isSlave() {
        return node.isSlave();
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if (node.isSlave()) {
            if (exchange.getIn().getBody() instanceof Status) {
                try {
                    getSlaveController().handleTweet((twitter4j.Status) exchange.getIn().getBody());
                } catch (Exception e) {
                    throw new SlaveCrashedException(e);
                }
            } else {
                throw new BadMessageException(exchange.getIn().getBody().toString());
            }
        }
    }

    public Node getNode() {
        return node;
    }
}


