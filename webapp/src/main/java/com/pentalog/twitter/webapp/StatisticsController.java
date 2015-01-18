package com.pentalog.twitter.webapp;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import twitter4j.JSONException;
import twitter4j.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by agherasim on 28/11/2014.
 */
@Controller
@RequestMapping("/")
public class StatisticsController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest,
					HttpServletResponse httpServletResponse) throws Exception {
		return null;
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView handleRequest() throws IOException {
		String url = "http://192.168.0.184:8020/getGraph/24.09.01.2015/14.10.01.2015/0/10";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		HttpResponse response = client.execute(request);

		System.out.println("Response Code : "
						+ response.getStatusLine().getStatusCode());

		BufferedReader rd = new BufferedReader(
						new InputStreamReader(response.getEntity().getContent()));

		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}


		ModelAndView modelAndView = new ModelAndView("index");
		modelAndView.addObject("result",result.toString());
		return modelAndView;
	}

	@RequestMapping(value="/home/getGraph",method = RequestMethod.GET,headers="Accept=application/json")
	public @ResponseBody String getGraph() throws IOException, JSONException {



		String url = "http://192.168.0.184:8020/getGraph/16.18.01.2015/19.18.01.2015/0/10";
		String result = httpClient(url);
//		ModelAndView modelAndView = new ModelAndView("blank");
//		modelAndView.addObject("result",result);
		return result;
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

		JSONObject jsonObj = new JSONObject(result.toString());
		return jsonObj.toString();
	}


}
