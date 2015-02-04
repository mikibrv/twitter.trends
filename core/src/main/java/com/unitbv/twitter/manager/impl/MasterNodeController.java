package com.unitbv.twitter.manager.impl;


import com.google.gson.Gson;
import com.unitbv.twitter.interfaces.IMasterNodeController;
import com.unitbv.twitter.manager.AbstractNodeController;
import com.unitbv.twitter.manager.wrapper.NodeProxy;
import com.unitbv.twitter.master.TweetBalancer;
import com.unitbv.twitter.pojo.Node;
import com.unitbv.twitter.pojo.TweetFilter;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;

/**
 * User: agherasim
 * Date: 11/11/2014
 * Time: 12:45 PM
 */
public class MasterNodeController extends AbstractNodeController implements IMasterNodeController {

    public MasterNodeController(Node node) {
        super(node);
    }

    TweetBalancer tweetBalancer;

    @EndpointInject(uri = "seda:masterDisplayFilteredTweets?size=10000")
    ProducerTemplate producer;


    @Override
    public boolean addNode(Node node) {
        super.addNode(node);
        LOGGER.warn("NODE ADDED:" + node);
        NodeProxy nodeProxy = this.getNodeByUUID(node.getUuid());
        nodeProxy.getSlaveController().ping(System.currentTimeMillis());
        nodeProxy.getSlaveController().filterSomeTweets(this.tweetFilter);
        nodeProxy.setCurrentNodeMasterController(this);
        try {
            if (tweetBalancer == null) {
                tweetBalancer = new TweetBalancer(null);
                camelContext.addRoutes(tweetBalancer);
            }
            tweetBalancer.addProcessor(nodeProxy);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void removeNode(String nodeID) {
        NodeProxy toRemove = getNodeByUUID(nodeID);
        if (toRemove != null) {
            Boolean isBroken = Boolean.TRUE;
            try {
                Long responseTime = toRemove.getSlaveController().ping(System.currentTimeMillis());
                if (responseTime != null && responseTime > 0) {
                    isBroken = false;
                }
            } catch (Exception e) {
                isBroken = true;
            }
            if (isBroken) {
                try {
                    toRemove.clearProxy();
                    if (tweetBalancer != null) {

                        tweetBalancer.removeProcessor(toRemove);
                    }
					nodeStats.removeNode(toRemove.getNode());
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void takeSomeTweets(TweetFilter tweetFilter) {
        producer.sendBody(new Gson().toJson(tweetFilter));
    }

    public void startTweetFiltering(String toFilter) {
        this.tweetFilter = new TweetFilter();
        this.tweetFilter.setToFilter(toFilter);
        for (NodeProxy nodeProxy : this.clusterNodes) {
            try {
                nodeProxy.getSlaveController().filterSomeTweets(tweetFilter);
            } catch (Exception e) {
                //do nothing
            }
        }

    }

}
