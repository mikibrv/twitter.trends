package com.pentalog.twitter.webapp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * User: mcsere
 * Date: 11/10/2014
 * Time: 6:48 PM
 */
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping
    public String handleRequest() {
        return "index";
    }

    @RequestMapping("/charts")
    public
    @ResponseBody
    String getChartData() throws IOException {
        // The fluent API relieves the user from having to deal with manual deallocation of system
// resources at the cost of having to buffer response content in memory in some cases.
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject("http://10.10.0.181:8021/getGraph/1/1", String.class);
    }

    @RequestMapping("/getTweets/{toFilter}")
    public
    @ResponseBody
    String getTweets(@PathVariable String toFilter) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://10.10.0.181:8020/startFiltering/?toFilter=" + toFilter, String.class);
    }

    @RequestMapping("/getTweets/")
    public
    @ResponseBody
    String stopTweets() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("http://10.10.0.181:8020/startFiltering/", String.class);
    }
}
