package com.unitbv.twitter.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import twitter4j.JSONArray;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by agherasim on 28/11/2014.
 */
@Controller
@RequestMapping("/")
public class StatisticsController extends AbstractController {

	@Value("${master.ip}")
	private String masterIP;
	@Value("${master.port}")
	private String masterPort;
	private String masterAddress;
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
					HttpServletResponse httpServletResponse) throws Exception {

		return null;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView handleRequest() throws IOException {
		masterAddress = masterIP+":"+masterPort;
		System.out.println(masterAddress);

		//		String url = "http://192.168.0.184:8020/getGraph/24.09.01.2015/14.10.01.2015/0/10";
//
//		HttpClient client = HttpClientBuilder.create().build();
//		HttpGet request = new HttpGet(url);
//
//		// add request header
//		HttpResponse response = client.execute(request);
//
//		System.out.println("Response Code : "
//						+ response.getStatusLine().getStatusCode());
//
//		BufferedReader rd = new BufferedReader(
//						new InputStreamReader(response.getEntity().getContent()));
//
//		StringBuffer result = new StringBuffer();
//		String line = "";
//		while ((line = rd.readLine()) != null) {
//			result.append(line);
//		}
		ModelAndView modelAndView = new ModelAndView("index");
//		modelAndView.addObject("result",result.toString());
		return modelAndView;
	}

	@RequestMapping(value="/home/getGraph",method = RequestMethod.GET,headers="Accept=application/json")
	public @ResponseBody String getGraph() throws IOException, JSONException {

		Calendar rightNow = Calendar.getInstance();
		int hour = rightNow.get(Calendar.HOUR_OF_DAY);
		int day = rightNow.get(Calendar.DAY_OF_MONTH);
		int month = rightNow.get(Calendar.MONTH)+1;
		int year = rightNow.get(Calendar.YEAR);
		String beginDate=hour+"."+day+"."+month+"."+year;//"16.18.01.2015"

		rightNow.add(Calendar.HOUR_OF_DAY, -11);

		hour = rightNow.get(Calendar.HOUR_OF_DAY);
		day = rightNow.get(Calendar.DAY_OF_MONTH);
		month = rightNow.get(Calendar.MONTH)+1;
		year = rightNow.get(Calendar.YEAR);
		String endDate=hour+"."+day+"."+month+"."+year;//"23.18.01.2015"

		String url = "http://"+masterAddress+"/getGraph/"+endDate+"/"+beginDate+"/0/10";
		String result = httpClient(url);
		return result;
	}

	@RequestMapping(value="/home/stats",method = RequestMethod.GET,headers="Accept=application/json")
	public @ResponseBody String getStats() throws IOException, JSONException {

		String url = "http://"+masterAddress+"/masterStats";
		String masterResult = httpClient(url);
		JSONObject masterJson = new JSONObject(masterResult);
		Integer masterTweetsProcessed= (Integer) masterJson.get("TweetsProcessed");

		JSONArray slaveTweetsProcesseedArray=new JSONArray();
		JSONArray slaveWordsProcessedArray=new JSONArray();
		long totalWordsProcessed=0;

		JSONArray nodes = (JSONArray) masterJson.get("nodes");
		for(int i=0;i<nodes.length();i++){
			String slaveAdress = (String) nodes.get(i);
			try {
				String slaveUrl = "http://" + slaveAdress + "/stats";
				String slaveResult = httpClient(slaveUrl);
				JSONObject slaveJson = new JSONObject(slaveResult);
				try {
					Integer slaveTweetsProcessed = (Integer) slaveJson.get("TweetsProcessed");
					JSONArray tweetsProcessed = new JSONArray();
					tweetsProcessed.put(slaveAdress);
					tweetsProcessed.put(slaveTweetsProcessed);
					slaveTweetsProcesseedArray.put(tweetsProcessed);
				}
				catch (Exception e) {
				}
				try {
					Integer slaveWordsProcessed = (Integer) slaveJson.get("WordsProcessed");
					totalWordsProcessed+=slaveWordsProcessed;
					JSONArray wordsProcessed = new JSONArray();
					wordsProcessed.put( slaveAdress);
					wordsProcessed.put(slaveWordsProcessed);
					slaveWordsProcessedArray.put(wordsProcessed);
				}
				catch (Exception e) {
				}
			}catch (Exception e){}
		}

		JSONObject result=new JSONObject();
		result.put("masterTweetsProcessed", masterTweetsProcessed);
		result.put("slaveTweetsProcessed", slaveTweetsProcesseedArray);
		result.put("totalWordsProcessed", totalWordsProcessed);
		result.put("slaveWordsProcessed", slaveWordsProcessedArray);


		return result.toString();
	}

	@RequestMapping(value="/home/getLastTweet",method = RequestMethod.GET)
	public @ResponseBody String getLastTweet() throws IOException, JSONException {

		String url = "http://"+masterAddress+"/masterStats";
		String masterResult = httpClient(url);
		String slaveResult="";
		JSONObject masterJson = new JSONObject(masterResult);
		JSONArray nodes = (JSONArray) masterJson.get("nodes");
		if(nodes.length()>0) {
			String slaveAdress = (String) nodes.get(0);
			try {
				String slaveUrl = "http://" + slaveAdress + "/getLastTweet";
				slaveResult = httpClient(slaveUrl);
			}
				catch (Exception e) {
			}
		}
		return slaveResult;
	}

	public String httpClient(String url) throws IOException, JSONException {

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		HttpResponse response = client.execute(request);
		response.addHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
		response.addHeader("Access-Control-Allow-Headers", "Content-Type");
		response.addHeader("Access-Control-Max-Age", "1800");//30 min
		BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
	}


}
