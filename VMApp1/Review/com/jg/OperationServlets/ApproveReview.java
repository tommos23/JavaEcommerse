package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ReviewController;

/**
 * Servlet implementation class UserValidate
 */
public class ApproveReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApproveReview() {
		super();
	}
	
	@SuppressWarnings("incomplete-switch")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		int review_id = Integer.parseInt(request.getParameter("review_id"));
		ReviewController rc = new ReviewController();
		rc.startSession();
			switch(rc.approveReview(review_id)){
			case SUCCESS:
				session.setAttribute("alertMessage","Review Updated.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("ViewArticleRevisions?article_id="+request.getParameter("article_id"));
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Review not updated.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewArticleRevisions?article_id="+request.getParameter("article_id"));
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewArticleRevisions?article_id="+request.getParameter("article_id"));
				break;
			}
		if(rc.isSessionReady())
			rc.endSession();
		return;
	}
}
