package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.manager.impl.MasterNodeManager;
import com.pentalog.twitter.manager.impl.SlaveNodeManager;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.stream.TwitterStreamRouteBuilder;
import org.apache.camel.model.ModelCamelContext;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 4:02 PM
 */
public class NodeManagerBuilder {

    private Node node;
    private ModelCamelContext camelContext;
    private TwitterStreamRouteBuilder twitterStreamRouteBuilder;

    public NodeManagerBuilder setNode(Node node) {
        this.node = node;
        return this;
    }

    public NodeManagerBuilder setCamelContext(ModelCamelContext modelCamelContext) {
        this.camelContext = modelCamelContext;
        return this;
    }

    public NodeManagerBuilder setStreamRouteBuilder(TwitterStreamRouteBuilder streamRouteBuilder) {
        this.twitterStreamRouteBuilder = streamRouteBuilder;
        return this;
    }


    public MasterNodeManager buildMaster() {
        MasterNodeManager nodeManager = new MasterNodeManager(node);
        nodeManager.setCamelContext(camelContext);
        nodeManager.setTwitterStreamRouteBuilder(twitterStreamRouteBuilder);
        return nodeManager;
    }

    public SlaveNodeManager buildSlave() {
        SlaveNodeManager nodeManager = new SlaveNodeManager(node);
        nodeManager.setCamelContext(camelContext);
        return nodeManager;
    }

}
