package com.unitbv.twitter.manager.enums;

/**
 * User: agherasim
 * Date: 11/11/2014
 * Time: 5:53 PM
 */
public abstract class RouteConstants {

    public static String ROUTE_MASTER_STREAM = "masterTwitterStream";

    public static String SLAVE_QUEUE = "SLAVE";

    public static String MASTER_QUEUE = "MASTER";

    public static String JMS_PROTOCOL = "tcp";

    public static String MASTER_LB_TO_SLAVES = "MasterLoadBalancer";

    public static String MASTER_TWEETS_QUEUE = "tweetsQueue";
}
