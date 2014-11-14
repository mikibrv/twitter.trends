package com.pentalog.twitter;

import org.apache.camel.model.ModelCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:24 PM
 */
public class NodeLauncher {

    public static String NODE_TYPE;

    public static void main(String[] args) throws InterruptedException {
        if (args.length > 0) {
            NODE_TYPE = args[0];
        }
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("core/applicationContext.xml");
        ModelCamelContext context = (ModelCamelContext) applicationContext.getBean("camelContext");
    }
}
