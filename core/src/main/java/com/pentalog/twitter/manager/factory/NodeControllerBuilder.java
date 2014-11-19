package com.pentalog.twitter.manager.factory;

import com.pentalog.twitter.interfaces.IMasterNodeController;
import com.pentalog.twitter.interfaces.ISlaveNodeController;
import com.pentalog.twitter.manager.impl.MasterNodeController;
import com.pentalog.twitter.manager.impl.SlaveNodeController;
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
        MasterNodeController masterNode = new MasterNodeController(null);
        return masterNode;
    }

    public ISlaveNodeController buildSlave() {
        SlaveNodeController slaveNode = new SlaveNodeController(null);
        return slaveNode;
    }

}
