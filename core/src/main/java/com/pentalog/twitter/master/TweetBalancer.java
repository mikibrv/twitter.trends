package com.pentalog.twitter.master;

import com.pentalog.twitter.manager.enums.RouteConstants;
import com.pentalog.twitter.manager.exceptions.SlaveCrashedException;
import com.pentalog.twitter.manager.wrapper.NodeProxy;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.loadbalancer.FailOverLoadBalancer;
import org.apache.camel.processor.loadbalancer.RoundRobinLoadBalancer;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 9:03 PM
 */
public class TweetBalancer extends RouteBuilder {

    protected Logger LOGGER = Logger.getLogger(this.getClass());

    private FailOverLoadBalancer loadBalancer;

    public TweetBalancer(List<NodeProxy> slaves) {
        //only slaves allowed
        loadBalancer = new FailOverLoadBalancer(new ArrayList<java.lang.Class<?>>() {
            {
                add(SlaveCrashedException.class);
            }
        });
        loadBalancer.setMaximumFailoverAttempts(2);
        loadBalancer.setRoundRobin(Boolean.TRUE);

        for (NodeProxy nodeProxy : slaves) {
            if (nodeProxy.getNode().isSlave()) {
                loadBalancer.addProcessor(nodeProxy);
                LOGGER.warn("ADDED SLAVE TO LB: " + nodeProxy.getNode().getUuid());
            }
        }
    }

    @Override
    public void configure() throws Exception {

        from("seda:" + RouteConstants.MASTER_TWEETS_QUEUE)
                .routeId(RouteConstants.MASTER_LB_TO_SLAVES)
                .loadBalance(loadBalancer)
                .to("seda:" + RouteConstants.MASTER_TWEETS_QUEUE); //send it back to the queue
    }
}
