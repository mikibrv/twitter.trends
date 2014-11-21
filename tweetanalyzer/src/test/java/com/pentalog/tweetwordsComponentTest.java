package com.pentalog;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;

public class tweetwordsComponentTest extends CamelTestSupport {

    public void testtweetwords() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedMinimumMessageCount(1);       
        
        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            public void configure() {
                from("tweetanalyzer://foo")
                  .to("tweetanalyzer://bar")
                  .to("mock:result");
            }
        };
    }
}
