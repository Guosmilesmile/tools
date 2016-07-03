package com.linyu.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有Servlet的基类
 * 
 * @author 
 *
 */
public abstract class BasicServlet extends HttpServlet {

	protected ServletReflection servletRef;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		
		servletRef = new ServletReflection(this, request.getParameter("method"));
		servletRef.invoke(request, response);
	}
}
