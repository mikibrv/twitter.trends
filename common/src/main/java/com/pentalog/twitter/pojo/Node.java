package com.pentalog.twitter.pojo;

import java.io.Serializable;
import java.util.UUID;

/**
 * User: mcsere
 * Date: 11/6/2014
 * Time: 12:17 PM
 */
public class Node implements Serializable {

    private String uuid;

    private String IP;

    private NodeType type = NodeType.SLAVE;

    public Node(String uuid, String IP) {
        this.uuid = uuid;
        this.IP = IP;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Boolean isMaster() {
        return type != null && type.equals(NodeType.MASTER);
    }

    public Boolean isSlave() {
        return !isMaster();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return uuid.equals(node.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
