package com.pentalog.twitter.slave;

import com.pentalog.twitter.manager.IMasterNodeManager;
import com.pentalog.twitter.manager.INodeManager;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spring.remoting.CamelProxyFactoryBean;

/**
 * User: mcsere
 * Date: 11/11/2014
 * Time: 9:20 PM
 */
public class SlaveToMasterRoute extends RouteBuilder {

    private String masterIP;
    private ModelCamelContext modelCamelContext;

    public SlaveToMasterRoute(String masterIP, ModelCamelContext modelCamelContext) {
        this.masterIP = masterIP;
        this.modelCamelContext = modelCamelContext;
    }

    @Override
    public void configure() throws Exception {

        CamelProxyFactoryBean proxyFactoryBean = new CamelProxyFactoryBean();
        proxyFactoryBean.setServiceInterface(IMasterNodeManager.class);
        proxyFactoryBean.setServiceUrl("activemq:queue:management");
        proxyFactoryBean.setCamelContext(modelCamelContext);
        proxyFactoryBean.setServiceRef("management");

       // proxyFactoryBean.afterPropertiesSet();


    }
}
