package com.kastkode.springsandwich.filter.coldcuts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kastkode.springsandwich.filter.api.AfterHandler;
import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;

@Component
public class AfterExample implements AfterHandler {

	@Override
    public void handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, ModelAndView modelAndView, String[] args) throws Exception {
		System.out.println("InterceptAfter called");
	}
}
