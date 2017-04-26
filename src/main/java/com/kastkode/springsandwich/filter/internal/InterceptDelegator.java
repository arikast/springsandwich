package com.kastkode.springsandwich.filter.internal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kastkode.springsandwich.filter.annotation.After;
import com.kastkode.springsandwich.filter.annotation.AfterElement;
import com.kastkode.springsandwich.filter.annotation.Before;
import com.kastkode.springsandwich.filter.annotation.BeforeElement;
import com.kastkode.springsandwich.filter.api.AfterHandler;
import com.kastkode.springsandwich.filter.api.BeforeHandler;
import com.kastkode.springsandwich.filter.api.Flow;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class InterceptDelegator extends HandlerInterceptorAdapter {

	@Autowired
	ApplicationContext appContext;

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Before classInterceptors = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Before.class);
        if (! preHandleInterceptors(classInterceptors, request, response, handlerMethod)) {
        	return false;
        }

	    Before methodInterceptors = handlerMethod.getMethod().getAnnotation(Before.class);
        if (! preHandleInterceptors(methodInterceptors, request, response, handlerMethod)) {
        	return false;
        }

        //currently just returns true, but who knows what necessary side-effects may be added in future
        return super.preHandle(request, response, handler);
    }

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (!(handler instanceof HandlerMethod)) return;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
	    After classInterceptors = handlerMethod.getMethod().getDeclaringClass().getAnnotation(After.class);
        postHandleInterceptors(classInterceptors, request, response, handlerMethod, modelAndView);

	    After methodInterceptors = handlerMethod.getMethod().getAnnotation(After.class);
        postHandleInterceptors(methodInterceptors, request, response, handlerMethod, modelAndView);
        
        //currently a nop, but who knows what required side-effects may be added in future
        super.postHandle(request, response, handler, modelAndView);
	}

	private boolean preHandleInterceptors(Before interceptors, HttpServletRequest request, HttpServletResponse response,
		HandlerMethod handlerMethod) throws Exception {
		if(interceptors == null) return true;

		Flow result = Flow.CONTINUE;
		for(BeforeElement classWithArgs:interceptors.value()) {
			BeforeHandler interceptInstance = appContext.getBean(classWithArgs.value());
			result = interceptInstance.handle(request, response, handlerMethod, classWithArgs.flags());
			if(result != Flow.CONTINUE) break;
    	}
		return (result == Flow.CONTINUE);
	}

	private void postHandleInterceptors(After interceptors, HttpServletRequest request, HttpServletResponse response,
		HandlerMethod handlerMethod, ModelAndView modelAndView) throws Exception {
		if(interceptors == null) return;

		for(AfterElement classWithArgs:interceptors.value()) {
	    	AfterHandler interceptInstance = appContext.getBean(classWithArgs.value());
	    	interceptInstance.handle(request, response, handlerMethod, modelAndView, classWithArgs.flags());
	    }
	}
}
