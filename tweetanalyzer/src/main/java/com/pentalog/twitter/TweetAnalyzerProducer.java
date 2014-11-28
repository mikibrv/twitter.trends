package com.pentalog.twitter;

import com.mongodb.DBObject;
import com.pentalog.twitter.model.mongoObjects.MongoProperties;
import com.pentalog.twitter.mongo.MongoStatistics;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultMessage;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The tweetanalyzer producer.
 */
public class TweetAnalyzerProducer extends DefaultProducer {

	private static final Logger LOG = LoggerFactory.getLogger(TweetAnalyzerProducer.class);

	private TweetAnalyzerEndpoint endpoint;

	private static int count = 0;

	public TweetAnalyzerProducer(TweetAnalyzerEndpoint endpoint) {

		super(endpoint);
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
	}

	private void tweetWordsOperation(Exchange exchange) {
		if (exchange.getIn().getBody() instanceof Status) {
			try {
				Status tweet = (Status) exchange.getIn().getBody();
				MongoStatistics.processWords(tweet);
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
}
