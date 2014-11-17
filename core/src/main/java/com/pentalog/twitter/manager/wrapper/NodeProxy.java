package com.pentalog.twitter.manager.wrapper;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.util.NodeUtil;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.model.ModelCamelContext;

/**
 * User: mcsere
 * Date: 11/17/2014
 * Time: 4:53 PM
 */
public class NodeProxy {

    private Node node;
    private ISlaveNodeController slaveNodeController;
    private IMasterNodeController masterNodeController;

    public NodeProxy(Node node) {
        this.node = node;
    }

    public void buildProxy(ModelCamelContext context) throws Exception {
        NodeUtil.addActiveMQComponent(node.getUuid(), NodeUtil.getJMSPathFromIP(node.getIP(), node.getJMSPort()), context);
        this.slaveNodeController = NodeUtil.buildProxySlave(node, context);
        this.masterNodeController = NodeUtil.buildProxyMaster(node, context);
    }

    public void removeProxy(ModelCamelContext camelContext) {
        camelContext.removeComponent(node.getUuid());
        this.slaveNodeController = null;
        this.masterNodeController = null;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public ISlaveNodeController getSlaveNodeController() {
        return slaveNodeController;
    }

    public void setSlaveNodeController(ISlaveNodeController slaveNodeController) {
        this.slaveNodeController = slaveNodeController;
    }

    public IMasterNodeController getMasterNodeController() {
        return masterNodeController;
    }

    public void setMasterNodeController(IMasterNodeController masterNodeController) {
        this.masterNodeController = masterNodeController;
    }
}


