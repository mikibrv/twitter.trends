package com.pentalog.twitter;

import com.pentalog.twitter.manager.exceptions.NOInternetException;
import com.pentalog.twitter.pojo.NodeType;
import com.pentalog.twitter.util.NodeUtil;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import java.util.Date;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:24 PM
 */
public class NodeLauncher {


    public static void main(String[] args) throws InterruptedException, NOInternetException {

		System.out.println(new Date(1416560400000L));
//        new ClassPathXmlApplicationContext(
//                new String[]{"core/applicationContext.xml"});
    }
}
