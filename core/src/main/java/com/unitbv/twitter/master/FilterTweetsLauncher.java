package com.unitbv.twitter.master;

import com.unitbv.twitter.interfaces.IMasterNodeController;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * User: agherasim
 * Date: 11/21/2014
 * Time: 9:39 PM
 */
@Component("filterTweetsLauncher")
public class FilterTweetsLauncher implements Processor {


    @Resource
    private IMasterNodeController masterNodeController;

    @Override
    public void process(Exchange exchange) throws Exception {
        try {
            String toFilter = (String) exchange.getIn().getHeaders().get("toFilter");
            masterNodeController.startTweetFiltering(toFilter);
        } catch (Exception e) {
            //
        }
    }
}
