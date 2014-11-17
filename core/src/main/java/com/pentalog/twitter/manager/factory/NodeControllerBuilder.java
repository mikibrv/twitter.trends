package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.impl.MasterNodeControllerController;
import com.pentalog.twitter.manager.impl.SlaveNodeControllerController;
import org.apache.camel.model.ModelCamelContext;

/**
 * User: mcsere
 * Date: 11/14/2014
 * Time: 5:33 PM
 */
public class NodeControllerBuilder {

    private ModelCamelContext camelContext;

    public NodeControllerBuilder setCamelContext(ModelCamelContext camelContext) {
        this.camelContext = camelContext;
        return this;
    }

    public IMasterNodeController buildMaster() {
        MasterNodeControllerController masterNode = new MasterNodeControllerController();
        return masterNode;
    }

    public ISlaveNodeController buildSlave() {
        SlaveNodeControllerController slaveNode = new SlaveNodeControllerController();
        return slaveNode;
    }

}
