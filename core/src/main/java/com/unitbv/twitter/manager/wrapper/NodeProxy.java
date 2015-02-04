package com.unitbv.twitter.manager.wrapper;

import com.unitbv.twitter.interfaces.IMasterNodeController;
import com.unitbv.twitter.interfaces.INodeController;
import com.unitbv.twitter.interfaces.ISlaveNodeController;
import com.unitbv.twitter.manager.enums.RouteConstants;
import com.unitbv.twitter.manager.exceptions.BadMessageException;
import com.unitbv.twitter.manager.exceptions.SlaveCrashedException;
import com.unitbv.twitter.pojo.Node;
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
 * User: agherasim
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
        final String brokerTo = getPathTOProxyJMS(node.getIP(), node.getJMSPort());
        ActiveMQConfiguration activeMQConfiguration = new ActiveMQConfiguration();
        activeMQConfiguration.setUseSingleConnection(true);
        activeMQConfiguration.setUsePooledConnection(false);
        activeMQConfiguration.setPreserveMessageQos(false);
        activeMQConfiguration.setAcceptMessagesWhileStopping(false);
        activeMQConfiguration.setConnectionFactory(new PooledConnectionFactory() {
            {
                setConnectionFactory(new ActiveMQConnectionFactory() {
                    {
                        setBrokerURL(brokerTo);
                        setUseAsyncSend(true);
                        setCopyMessageOnSend(false);
                        setCloseTimeout(1000);
                    }

                });
                setMaxConnections(1);
            }
        });
        // activeMQConfiguration.set
        camelContext.addComponent(node.getUuid(), new ActiveMQComponent(activeMQConfiguration));
        LOGGER.warn("Added ActiveMQComponent to: " + getPathTOProxyJMS(node.getIP(), node.getJMSPort()));
        //build the proxies depending on nodeType;
        if (this.node.isMaster()) {
            this.remoteNodeController = new ProxyBuilder(camelContext).
                    endpoint(node.getUuid() + ":queue:" + RouteConstants.MASTER_QUEUE).
                    build(IMasterNodeController.class);
            LOGGER.warn("Created MasterProxy: " + node.getUuid());
        } else {
            this.remoteNodeController = new ProxyBuilder(camelContext).
                    endpoint(node.getUuid() + ":queue:" + RouteConstants.SLAVE_QUEUE).
                    build(ISlaveNodeController.class);
            LOGGER.warn("Created SlaveProxy" + node.getUuid());
        }
    }

    public void clearProxy() {
        try {
            ActiveMQComponent component = (ActiveMQComponent) camelContext.getComponent(node.getUuid());
            component.shutdown();
            component.stop();
        } catch (Exception e) {
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
        if (node.isSlave()) {
            if (exchange.getIn().getBody() instanceof Status) {
                try {
                    getSlaveController().handleTweet((twitter4j.Status) exchange.getIn().getBody());
                } catch (Exception e) {
                    LOGGER.warn("Node crashed: " + node.getUuid(), e);
                    currentNodeMasterController.removeNode(node.getUuid());
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


