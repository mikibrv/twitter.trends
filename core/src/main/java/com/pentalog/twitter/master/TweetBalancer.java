package com.pentalog.twitter.master;

import com.pentalog.twitter.pojo.Node;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.camel.component.ActiveMQConfiguration;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import java.util.List;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 9:03 PM
 */
public class TweetBalancer extends RouteBuilder {

    private List<Node> nodeList;

    public TweetBalancer(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public void configure() throws Exception {

    }

    public void addJMSBrokersForNodes() {
        ModelCamelContext camelContext = getContext();
        for (final Node node : nodeList) {
            camelContext.addComponent(node.getUuid(), new ActiveMQComponent(new ActiveMQConfiguration() {
                {
                    setBrokerURL("tcp://" + node.getIP());
                }
            }));
        }
    }
}
