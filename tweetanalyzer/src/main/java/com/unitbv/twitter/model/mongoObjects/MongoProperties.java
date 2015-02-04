package com.unitbv.twitter.model.mongoObjects;

/**
 * Created by agherasim on 25/11/2014.
 */
public class MongoProperties {

	private String host;

	private String port;

	public String getHost() {

		return host;
	}

	public void setHost(String host) {

		this.host = host;
	}

	public int getPort() throws NumberFormatException {

		return Integer.parseInt(port);
	}

	public void setPort(String port) {

		this.port = port;
	}
}
