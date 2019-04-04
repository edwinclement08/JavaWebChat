package com.javapapers.webservices.rest.jersey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RootRedirect
 */
public class RootRedirect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RootRedirect() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
//		response.setContentType("text/html;charset=UTF-8");
//
//		PrintWriter out = response.getWriter();
//		out.println("test");
//		out.close();
//

//		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/pages/build/index.html");
//		try {
//			dispatcher.forward(request, response);
//		} catch (ServletException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		response.sendRedirect(request.getContextPath() + "/pages/build/index.html");
	}

}
