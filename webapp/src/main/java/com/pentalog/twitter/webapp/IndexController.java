package com.pentalog.twitter.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
