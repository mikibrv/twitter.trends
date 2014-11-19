package com.pentalog.twitter.master;

import com.pentalog.twitter.manager.wrapper.NodeProxy;
import com.pentalog.twitter.pojo.Node;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.processor.loadbalancer.LoadBalancerSupport;
import org.apache.camel.processor.loadbalancer.SimpleLoadBalancerSupport;

import java.util.List;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 9:03 PM
 */
public class TweetBalancer extends SimpleLoadBalancerSupport {


    @Override
    public void process(Exchange exchange) throws Exception {

    }
}
