package com.pentalog.twitter.pojo;

import java.util.Date;

/**
 * User: mcsere
 * Date: 11/20/2014
 * Time: 11:29 PM
 */
public class NodeStats {


	private int countProcessed;

	public int getCountProcessed() {

		return countProcessed;
	}

	public synchronized void incrementCount() {

		this.countProcessed++;
		if (countProcessed % 1000 == 0) {
			System.out.println(new Date().toString() + " " + countProcessed);
		}
	}

	public void setCountProcessed(int countProcessed) {

		this.countProcessed = countProcessed;
	}
}
