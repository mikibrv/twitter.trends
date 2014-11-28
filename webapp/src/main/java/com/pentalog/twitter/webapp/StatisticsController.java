package com.pentalog.twitter.webapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	public ModelAndView handleRequest() {

		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}
}
