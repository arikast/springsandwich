package com.kastkode.springsandwich.filter.annotation;

import java.lang.annotation.*;

import org.springframework.web.servlet.HandlerInterceptor;

import com.kastkode.springsandwich.filter.api.AfterHandler;
import com.kastkode.springsandwich.filter.api.BeforeHandler;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Before {
	BeforeElement[] value();
}
