package com.unitbv.twitter.manager;

import com.unitbv.twitter.interfaces.IMasterNodeController;
import com.unitbv.twitter.interfaces.ISlaveNodeController;
import com.unitbv.twitter.manager.enums.RouteConstants;
import com.unitbv.twitter.pojo.Node;
import com.unitbv.twitter.processor.TweetFilterProcessor;
import com.unitbv.twitter.util.NodeUtil;
import org.apache.camel.Route;
import org.apache.camel.model.ModelCamelContext;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: agherasim
 * Date: 11/17/2014
 * Time: 4:20 PM
 */
@Component("nodeManager")
public class NodeManager {

    Logger LOGGER = Logger.getLogger(this.getClass());

    @Resource(name = "camelContext")
    private ModelCamelContext camelContext;
    @Resource(name = "" +
					"")
    private Node node;
    @Value("${cluster.master.jms.port}")
    private Integer masterPORT;
    @Value("${cluster.master.IP}")
    private String masterIP;

    @Resource
    private TweetFilterProcessor tweetFilterProcessor;


    @Resource(name = "masterController")
    IMasterNodeController masterNodeController;

    @Resource(name = "slaveController")
    ISlaveNodeController slaveNodeController;


    public void buildNodes() {
        //if slave, call master
        switch (node.getType()) {
            case MASTER: {
                try {
                    this.startAsMaster();
                } catch (Exception e) {
                    LOGGER.fatal("Unable to start as Master", e);
                }
                break;
            }
            case SLAVE: {
                try {
                    this.startAsSlave();
                } catch (Exception e) {
                    LOGGER.fatal("Unable to start as Slave", e);
                }
                break;
            }
        }
    }

    public void startAsSlave() throws Exception {
        changeStateRoutes(RouteConstants.SLAVE_QUEUE, RouteConstants.MASTER_QUEUE);
        /**
         * Connect to the master;
         */
        Node masterNode = new Node(RouteConstants.MASTER_QUEUE, masterIP);
        masterNode.setJMSPort(masterPORT);
        NodeUtil.addActiveMQComponent(masterNode, camelContext);
        IMasterNodeController masterProxy = NodeUtil.buildProxyMaster(masterNode, camelContext);
        tweetFilterProcessor.setMasterNodeController(masterProxy);
        LOGGER.warn("PING REPLY: " + masterProxy.ping(System.currentTimeMillis()));
        masterProxy.addNode(node);
    }


    public void startAsMaster() throws Exception {
        changeStateRoutes(RouteConstants.MASTER_QUEUE, RouteConstants.SLAVE_QUEUE);
    }

    /**
     * Start routes containing toStart String in their ID;
     * Shut down routes countaining toShutDown String in their ID;
     *
     * @param toStart
     * @param toShutDown
     * @throws Exception
     */
    private void changeStateRoutes(String toStart, String toShutDown) throws Exception {
        for (Route route : camelContext.getRoutes()) {
            if (route.getId().toLowerCase().contains(toStart.toLowerCase())) {
                camelContext.startRoute(route.getId());
            } else if (route.getId().toLowerCase().contains((toShutDown.toLowerCase()))) {
                camelContext.stopRoute(route.getId());
            }
        }
    }
}
