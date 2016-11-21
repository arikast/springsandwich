package com.kastkode.springsandwich.filter.annotation;

import java.lang.annotation.*;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kastkode.springsandwich.filter.api.AfterHandler;
import com.kastkode.springsandwich.filter.api.BeforeHandler;

public @interface BeforeElement {
	Class<? extends BeforeHandler> value();
	String [] flags() default {};
}
