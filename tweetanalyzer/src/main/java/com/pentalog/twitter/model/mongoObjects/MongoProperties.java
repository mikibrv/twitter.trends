package com.pentalog.twitter.model.mongoObjects;

/**
 * Created by agherasim on 25/11/2014.
 */
public class MongoProperties {

	public String getHost() {

		return host;
	}

	public void setHost(String host) {

		this.host = host;
	}

	public int getPort() {

		return port;
	}

	public void setPort(int port) {

		this.port = port;
	}

	private String host;
	private int port;
}
