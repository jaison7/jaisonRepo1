package com.user.controller;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.user.service.impl.MocklogserviceDB;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/** This class will log all http requests and responses along with their correalation id, Http headers, 
 *  and other details.
 * @return
 */
@Component
public class HttpLoggingFilter extends OncePerRequestFilter	{


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		}catch(Exception e) {
			
		}
		finally {
			MocklogserviceDB mocklogserviceDB = new MocklogserviceDB();
			
			// create log service DB with all tables and populate values similar to below
			mocklogserviceDB.save(request.getRequestURL(),request.getMethod());
		}
		
		
		
	}
	
}
