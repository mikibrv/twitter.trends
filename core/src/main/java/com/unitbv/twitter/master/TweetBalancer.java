package com.unitbv.twitter.master;

import com.unitbv.twitter.manager.enums.RouteConstants;
import com.unitbv.twitter.manager.exceptions.SlaveCrashedException;
import com.unitbv.twitter.manager.wrapper.NodeProxy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.loadbalancer.FailOverLoadBalancer;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: agherasim
 * Date: 11/11/2014
 * Time: 9:03 PM
 */
public class TweetBalancer extends RouteBuilder {

    protected Logger LOGGER = Logger.getLogger(this.getClass());

    private RoundRobinLoadBalancer loadBalancer;

    public TweetBalancer(List<NodeProxy> slaves) {
        //only slaves allowed
        loadBalancer = new RoundRobinLoadBalancer();

        if (slaves != null) {
            for (NodeProxy nodeProxy : slaves) {
                if (nodeProxy.getNode().isSlave()) {
                    loadBalancer.addProcessor(nodeProxy);
                    LOGGER.warn("ADDED SLAVE TO LB: " + nodeProxy.getNode().getUuid());
                }
            }
        }
    }

    public void addProcessor(NodeProxy nodeProxy) {
        this.loadBalancer.addProcessor(nodeProxy);
    }

    @Override
    public void configure() throws Exception {

        from("seda:" + RouteConstants.MASTER_TWEETS_QUEUE)
                .routeId(RouteConstants.MASTER_LB_TO_SLAVES)
                .loadBalance(loadBalancer);
    }

    public void removeProcessor(NodeProxy nodeProxy) {
        this.loadBalancer.removeProcessor(nodeProxy);

    }
}
