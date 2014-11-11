package com.pentalog.twitter.manager.impl;

import com.pentalog.twitter.manager.AbstractNodeManager;
import com.pentalog.twitter.manager.IMasterNodeManager;
import com.pentalog.twitter.pojo.Node;
import com.pentalog.twitter.stream.TwitterStreamRouteBuilder;
import org.apache.camel.model.RoutesDefinition;

import java.io.InputStream;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class MasterNodeManager extends AbstractNodeManager implements IMasterNodeManager {

    private TwitterStreamRouteBuilder twitterStreamRouteBuilder;

    public MasterNodeManager(Node node) {
        super(node);
        clusterNodes.add(node);
    }

    @Override
    public Boolean isMaster() {
        return true;
    }

    @Override
    public void initRoutes() {
        super.initRoutes();
        try {
            this.camelContext.addRoutes(twitterStreamRouteBuilder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTwitterStreamRouteBuilder(TwitterStreamRouteBuilder twitterStreamRouteBuilder) {
        this.twitterStreamRouteBuilder = twitterStreamRouteBuilder;
    }
}
