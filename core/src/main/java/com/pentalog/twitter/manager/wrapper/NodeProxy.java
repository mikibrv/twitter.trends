package com.pentalog.twitter.manager.wrapper;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.INodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.manager.exceptions.BadMessageException;
import com.pentalog.twitter.manager.exceptions.SlaveCrashedException;
import com.pentalog.twitter.pojo.Node;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.activemq.pool.PooledConnectionFactory;
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
    private INodeController remoteNodeController = null;
    private ModelCamelContext camelContext;
    private IMasterNodeController currentNodeMasterController = null;


    public NodeProxy(Node node, ModelCamelContext camelContext) throws Exception {
        this.node = node;
        this.camelContext = camelContext;
        this.buildProxyOverNode();
    }

    public IMasterNodeController getMasterController() {
        if (this.isMaster()) {
            return (IMasterNodeController) this.remoteNodeController;
        }
        return null;
    }

    public ISlaveNodeController getSlaveController() {
        if (this.isSlave()) {
            return (ISlaveNodeController) this.remoteNodeController;
        }
        return null;
    }

    public void buildProxyOverNode() throws Exception {

    }

    public void clearProxy() {
        try {
            ActiveMQComponent component = (ActiveMQComponent) camelContext.getComponent(node.getUuid());
            component.shutdown();
            component.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        camelContext.removeComponent(node.getUuid());
        this.remoteNodeController = null;
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

    }

    public Node getNode() {
        return node;
    }

    public void setCurrentNodeMasterController(IMasterNodeController currentNodeMasterController) {
        this.currentNodeMasterController = currentNodeMasterController;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NodeProxy nodeProxy = (NodeProxy) o;

        if (node != null ? !node.equals(nodeProxy.node) : nodeProxy.node != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return node != null ? node.hashCode() : 0;
    }
}


