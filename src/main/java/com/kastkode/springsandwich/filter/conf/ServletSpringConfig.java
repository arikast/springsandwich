package com.kastkode.springsandwich.filter.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.kastkode.springsandwich.filter.internal.InterceptDelegator;

@Configuration
public class ServletSpringConfig extends WebMvcConfigurerAdapter {

		@Autowired
	    private InterceptDelegator interceptDelegator;
		
	    @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        registry.addInterceptor(
	        	interceptDelegator
	        );
	    }	 
}