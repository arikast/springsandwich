package com.kastkode.springsandwich.filter.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

@Component
public interface AfterHandler extends Handler {
	
	public void handle(HttpServletRequest request, HttpServletResponse response, 
			HandlerMethod handler, ModelAndView modelAndView, String[] flags) throws Exception;

}