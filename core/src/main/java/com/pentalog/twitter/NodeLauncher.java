package com.pentalog.twitter;

import com.pentalog.twitter.manager.INodeManager;
import com.pentalog.twitter.manager.factory.NodeManagerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:24 PM
 */
public class NodeLauncher {

    public static void main(String[] args) {
        NodeManagerFactory.setNodeType(args);  //from maven profile
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("core/applicationContext.xml");
    }
}
