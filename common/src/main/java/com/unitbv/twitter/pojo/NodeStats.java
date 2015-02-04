package com.unitbv.twitter.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: agherasim
 * Date: 11/20/2014
 * Time: 11:29 PM
 */
public class NodeStats{


	private int countProcessed;
	private long countWordProcessed;
	private List<Node> nodes;

	public NodeStats(){
		countProcessed=0;
		countWordProcessed=0;
		nodes=new ArrayList<>();
	}



	public synchronized void incrementCount() {

		this.countProcessed++;
		if (countProcessed % 1000 == 0) {
			System.out.println(new Date().toString() + " countProcessed: " + countProcessed);
		}
	}

	public synchronized void incrementCountWordProcessed(int nr) {

		this.countWordProcessed+=nr;
//		if (countWordProcessed % 1000 == 0) {
//			System.out.println(new Date().toString() + " countWordProcessed: " + countWordProcessed);
//		}
	}


	@Override public String toString() {

		StringBuilder stringBuilder=new StringBuilder();
		stringBuilder.append("{");
		stringBuilder.append("TweetsProcessed:").append(getCountProcessed());

		if(nodes.size()>0){
			stringBuilder.append(",nodes:");
			stringBuilder.append("[");
			for(Node node:nodes){
				String ip = node.getIP();
				Integer port = node.getPort();
				stringBuilder.append("\"").append(ip).append(":").append(port).append("\"").append(",");
			}
			stringBuilder.deleteCharAt(stringBuilder.length()-1);
			stringBuilder.append("]");
		}

		if(getCountWordProcessed()>0) {
			stringBuilder.append(",WordsProcessed:").append(getCountWordProcessed());
		}
		stringBuilder.append("}");

		return stringBuilder.toString();
	}

	public synchronized void addNode(Node node){
		nodes.add(node);
	}

	public synchronized void removeNode(Node node){
		nodes.remove(node);
	}

	public synchronized long getCountWordProcessed() {
		return countWordProcessed;
	}
	public synchronized int getCountProcessed() {
		return countProcessed;
	}
	public synchronized List<Node> getNodes() {

		return nodes;
	}

	public void setNodes(List<Node> nodes) {

		this.nodes = nodes;
	}
	public void setCountWordProcessed(long countWordProcessed) {

		this.countWordProcessed = countWordProcessed;
	}
	public void setCountProcessed(int countProcessed) {

		this.countProcessed = countProcessed;
	}
}
