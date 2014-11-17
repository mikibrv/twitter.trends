package com.pentalog.twitter;

import com.pentalog.twitter.manager.exceptions.NOInternetException;
import com.pentalog.twitter.pojo.NodeType;
import com.pentalog.twitter.util.NodeUtil;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:24 PM
 */
public class NodeLauncher {


    public static void main(String[] args) throws InterruptedException, NOInternetException {
        /**
         * Create the current node;
         */
        NodeType nodeType = NodeUtil.getNodeType(args);
        /**
         * Register the node as a bean in a parent context;
         */
        DefaultListableBeanFactory parentBeanFactory = new DefaultListableBeanFactory();
        parentBeanFactory.registerSingleton("currentNodeType", nodeType);
        GenericApplicationContext parentContext =
                new GenericApplicationContext(parentBeanFactory);
        parentContext.refresh();

        /**
         * Create the actual applicationContext, containing the parent context
         */
        new ClassPathXmlApplicationContext(
                new String[]{"core/applicationContext.xml"},
                parentContext);
    }
}
