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

    @Override
    public void configure() throws Exception {

    }


}
