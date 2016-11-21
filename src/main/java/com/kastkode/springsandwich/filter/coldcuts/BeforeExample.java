package com.kastkode.springsandwich.filter.coldcuts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;

@Component
public class BeforeExample implements BeforeHandler {

	@Override
    public Flow handle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, String[] flags) throws Exception {
		System.out.println("InterceptBefore called with following flags:");
		if(flags != null) {
			for(String arg:flags) {
				System.out.println(arg);
			}
		}
		return Flow.CONTINUE;
	}
}
