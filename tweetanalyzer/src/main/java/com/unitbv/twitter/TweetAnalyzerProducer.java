package com.unitbv.twitter;

import com.mongodb.DBObject;
import com.unitbv.twitter.model.mongoObjects.MongoProperties;
import com.unitbv.twitter.mongo.MongoStatistics;
import com.unitbv.twitter.pojo.NodeStats;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;

/**
 * The tweetanalyzer producer.
 */
public class TweetAnalyzerProducer extends DefaultProducer {

	private static final Logger LOG = LoggerFactory.getLogger(TweetAnalyzerProducer.class);

	private static String lastTweet;

	private TweetAnalyzerEndpoint endpoint;

	private static int count = 0;

	private static NodeStats nodeStats;

	public TweetAnalyzerProducer(TweetAnalyzerEndpoint endpoint) {

		super(endpoint);
		if(nodeStats==null)
			nodeStats=new NodeStats();
		this.endpoint = endpoint;
	}

	public void process(Exchange exchange) throws Exception {

		String endpointUri = endpoint.getEndpointUri();
		String command = endpointUri.substring(endpointUri.indexOf("://") + 3);
		MongoProperties mongoProperties = endpoint.getMongoProperties();
		MongoStatistics.init(mongoProperties);
		if (command.startsWith("tweetWords")) {
			tweetWordsOperation(exchange);
		}
		else if (command.startsWith("getGraph")) {
			getGraphOperation(exchange);
		}
		else if(command.startsWith("stats")){
			getStats(exchange);
		}else if(command.startsWith("getLastTweet")){
			getLastTweet(exchange);
		}

	}

	private void getLastTweet(Exchange exchange) {
		DefaultMessage message = new DefaultMessage();
		message.setBody(getLastTweet());
		exchange.setOut(message);

	}

	private void getStats(Exchange exchange) {
		DefaultMessage message = new DefaultMessage();
		message.setBody(nodeStats.toString());
		exchange.setOut(message);
	}

	private void tweetWordsOperation(Exchange exchange) {
		if (exchange.getIn().getBody() instanceof Status) {
			try {
				Status tweet = (Status) exchange.getIn().getBody();
				if(tweet.getLang().equals("en") &&tweet.getText().length()>0) {
					setLastTweet(tweet.getText());
				}
				nodeStats.incrementCount();
				int nrWords=MongoStatistics.processWords(tweet);
				nodeStats.incrementCountWordProcessed(nrWords);
			}catch (Exception e){
				LOG.warn("There was a problem with processing this tweet!"+e.getMessage());
			}
		}
	}

	private void getGraphOperation(Exchange exchange) {

		DefaultMessage message = new DefaultMessage();
		try {
			String camelHttpUri = (String) exchange.getIn().getHeaders().get("CamelHttpUri");
			String[] split = camelHttpUri.split("/");

			long minBeginDate = getDateFormatForGetGraph().parse(split[2]).getTime();
			long maxBeginDate = getDateFormatForGetGraph().parse(split[3]).getTime();
			int skip = Integer.parseInt(split[4]);
			int limit = Integer.parseInt(split[5]);
			DBObject graphData = MongoStatistics.getGraphData(minBeginDate, maxBeginDate, skip, limit);
			message.setBody(graphData);
		}
		catch (Exception e) {

			StringBuilder infoMessage=new StringBuilder();
			infoMessage.append("The parameters are incorrect!\n");
			infoMessage.append("Insert getGraph");
			infoMessage.append("/\"date minDateInterval\"");
			infoMessage.append("/\"date maxDateInterval\"");
			infoMessage.append("/\"int skip\"");
			infoMessage.append("/\"int limit\"");
			infoMessage.append("\n");
			infoMessage.append("Pattern for the date:");
			infoMessage.append(getDateFormatForGetGraph().toPattern());
			infoMessage.append("\nHH:hour");
			infoMessage.append("\ndd:day");
			infoMessage.append("\nMM:Month");
			infoMessage.append("\nyyyy:year");

			message.setBody(infoMessage.toString());
		}
		exchange.setOut(message);
	}

	private SimpleDateFormat getDateFormatForGetGraph(){
		return new SimpleDateFormat("HH.dd.MM.yyyy");
	}

	public String getLastTweet() {

		return lastTweet;
	}

	public void setLastTweet(String lastTweet) {

		this.lastTweet = lastTweet;
	}
}
