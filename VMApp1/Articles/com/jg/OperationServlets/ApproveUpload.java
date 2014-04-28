package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ArticleController;

/**
 * Servlet implementation class UserValidate
 */
public class ApproveUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApproveUpload() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		int id = 0;
		if (request.getParameter("id") != null) 
			id = Integer.parseInt(request.getParameter("id"));
		ArticleController ac = new ArticleController();
		ac.startSession();
		switch(ac.approveUpload(id)){
		case SUCCESS:
			session.setAttribute("alertMessage","Article is successfully approved.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("ApproveArticles");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check article number.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ApproveArticles");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ApproveArticles");
			break;
		}
		if(ac.isSessionReady())
			ac.endSession();
	}
}
