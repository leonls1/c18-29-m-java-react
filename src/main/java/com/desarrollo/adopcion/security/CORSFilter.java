package com.desarrollo.adopcion.security;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CORSFilter implements Filter {
	
	 @Override
	 public void init(FilterConfig filterConfig) throws ServletException {

	 }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletResponse htresponse = (HttpServletResponse) response;
		htresponse.setHeader("Access-Control-Allow-Origin", "*");
		htresponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE, PATCH");
        htresponse.setHeader("Access-Control-Max-Age", "3600");
        htresponse.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");
        htresponse.setHeader("Access-Control-Expose-Headers", "Location");
        chain.doFilter(request, response);
		
	}

	@Override
    public void destroy() {

    }
}
