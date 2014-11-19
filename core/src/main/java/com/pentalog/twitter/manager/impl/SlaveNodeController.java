package com.pentalog.twitter.manager.impl;


import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.AbstractNodeController;
import com.pentalog.twitter.pojo.Node;
import twitter4j.Status;

import java.util.List;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class SlaveNodeController extends AbstractNodeController implements ISlaveNodeController {


    @Override
    public int handleTweet(Status tweet) {
        LOGGER.warn(tweet);
        return 0;
    }

}
