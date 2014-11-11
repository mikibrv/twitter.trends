package com.pentalog.twitter.stream;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.twitter.TwitterComponent;
import org.apache.camel.model.ModelCamelContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Properties;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 3:14 PM
 */
@Component("twitterStreamRouteBuilder")
public class TwitterStreamRouteBuilder extends RouteBuilder {

    @Resource(name = "twitterProps")
    Properties twitterProps;

    public TwitterStreamRouteBuilder() {
    }

    @Override
    public void configure() throws Exception {
        this.configureAccess();
        String twitterRoute = twitterProps.getProperty("twitter.route");
        twitterRoute = String.format(twitterRoute, twitterProps.getProperty("twitter.stream.delay"),
                twitterProps.getProperty("twitter.stream.type"));
        from(twitterRoute).loadBalance().roundRobin().to("bean:twitterBean");
    }

    private void configureAccess() {
        TwitterComponent twitterComponent = getContext().getComponent("twitter", TwitterComponent.class);
        twitterComponent.setAccessToken(twitterProps.getProperty("twitter.stream.accessToken"));
        twitterComponent.setAccessTokenSecret(twitterProps.getProperty("twitter.stream.accessTokenSecret"));
        twitterComponent.setConsumerKey(twitterProps.getProperty("twitter.stream.consumerKey"));
        twitterComponent.setConsumerSecret(twitterProps.getProperty("twitter.stream.consumerSecret"));
    }
}
