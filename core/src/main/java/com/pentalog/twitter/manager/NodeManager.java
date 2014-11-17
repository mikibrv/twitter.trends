package com.pentalog.twitter.manager;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.util.NodeUtil;
import org.apache.camel.Route;
import org.apache.camel.model.ModelCamelContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: mcsere
 * Date: 11/17/2014
 * Time: 4:20 PM
 */
@Component("nodeManager")
public class NodeManager {

    @Resource(name = "camelContext")
    private ModelCamelContext camelContext;
    @Resource(name = "currentNode")
    private Node node;
    @Value("${cluster.master.jms.port}")
    private Integer masterPORT;
    @Value("${cluster.master.IP}")
    private String masterIP;


    @Resource(name = "masterController")
    IMasterNodeController masterNodeController;

    @Resource(name = "slaveController")
    ISlaveNodeController slaveNodeController;


    public void buildNodes() {
        String x = "";
        //if slave, call master
        switch (node.getType()) {
            case MASTER: {
                try {
                    camelContext.startRoute(RouteConstants.ROUTE_MASTER_STREAM);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case SLAVE: {
                Node masterNode = new Node(RouteConstants.MASTER_QUEUE, masterIP);
                masterNode.setJMSPort(masterPORT);
                NodeUtil.addActiveMQComponent(RouteConstants.MASTER_QUEUE, NodeUtil.getJMSPathFromIP(masterNode.getIP(), masterNode.getJMSPort()), camelContext);
                try {
                    IMasterNodeController masterProxy = NodeUtil.buildProxyMaster(masterNode, camelContext);
                    masterProxy.addNode(node);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}
