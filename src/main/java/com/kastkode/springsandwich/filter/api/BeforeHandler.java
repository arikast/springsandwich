package com.kastkode.springsandwich.filter.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public interface BeforeHandler extends Handler {

	public Flow handle(HttpServletRequest request, HttpServletResponse response, 
			HandlerMethod handler, String[] flags) throws Exception;

}