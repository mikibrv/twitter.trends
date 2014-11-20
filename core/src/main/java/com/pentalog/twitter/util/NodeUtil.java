package com.pentalog.twitter.util;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.pojo.NodeType;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.builder.ProxyBuilder;
import org.apache.camel.model.ModelCamelContext;

/**
 * User: mcsere
 * Date: 11/17/2014
 * Time: 5:05 PM
 */
public class NodeUtil {

    public static NodeType getNodeType(String[] args) {
        String nodeType = args.length > 0 ? args[0] : "";
        if (NodeType.MASTER.toString().equalsIgnoreCase(nodeType)) {
            return NodeType.MASTER;
        } else {
            return NodeType.SLAVE;
        }
    }

    public static ISlaveNodeController buildProxySlave(Node node, ModelCamelContext context) throws Exception {
        return new ProxyBuilder(context).endpoint(node.getUuid() + ":queue:" + RouteConstants.SLAVE_QUEUE).build(ISlaveNodeController.class);
    }

    public static IMasterNodeController buildProxyMaster(Node node, ModelCamelContext context) throws Exception {
        return new ProxyBuilder(context).endpoint(node.getUuid() + ":queue:" + RouteConstants.MASTER_QUEUE).build(IMasterNodeController.class);
    }

    /**
     * @param node    tcp://localhost:61616
     * @param context
     */
    public static void addActiveMQComponent(Node node, ModelCamelContext context) {
        ActiveMQConfiguration activeMQConfiguration = new ActiveMQConfiguration();
        activeMQConfiguration.setBrokerURL(getJMSPathFromIP(node.getIP(), node.getJMSPort()));
        activeMQConfiguration.setAutoStartup(true);
        context.addComponent(node.getUuid(), new ActiveMQComponent(activeMQConfiguration));
    }

    public static String getJMSPathFromIP(String ip, Integer port) {
        return RouteConstants.JMS_PROTOCOL + "://" + ip + ":" + port;
    }
}
