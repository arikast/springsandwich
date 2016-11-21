package com.kastkode.springsandwich.filter.annotation;

import java.lang.annotation.*;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kastkode.springsandwich.filter.api.AfterHandler;
import com.kastkode.springsandwich.filter.api.BeforeHandler;

public @interface AfterElement {
	Class<? extends AfterHandler> value();
	String [] flags() default {};
}
