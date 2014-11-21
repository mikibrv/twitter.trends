package com.pentalog.twitter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import javax.servlet.http.HttpServletRequest;

/**
 * User: mcsere
 * Date: 11/6/2014
 * Time: 10:42 AM
 */
public class HTTPProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        // just get the body as a string
        String body = exchange.getIn().getBody(String.class);

        // we have access to the HttpServletRequest here and we can grab it if we need it
        HttpServletRequest req = exchange.getIn().getBody(HttpServletRequest.class);

        // send a html response
        exchange.getOut().setBody("<html><body>Camel in Action</body></html>");
    }

}
