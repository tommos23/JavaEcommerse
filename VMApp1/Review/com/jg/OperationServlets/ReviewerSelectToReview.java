package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ArticleController;
import com.jg.Controller.ReviewController;
import com.jg.Controller.UserController;
import com.jg.Model.Article;
import com.jg.Model.User;

/**
 * Servlet implementation class UserValidate
 */
public class ReviewerSelectToReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewerSelectToReview() {
		super();
	}
	
	@SuppressWarnings("incomplete-switch")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		
		ArticleController ac = new ArticleController();
		ac.startSession();
		Article article = ac.get(Integer.parseInt(request.getParameter("article_id")));
		if(ac.isSessionReady())
			ac.endSession();
		
		UserController uc = new UserController();
		uc.startSession();	
		User reviewer = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		if(uc.isSessionReady())
			uc.endSession();
		
		ReviewController rc = new ReviewController();
		rc.startSession();
		switch(rc.create("", "", -1, -1, article, reviewer)){
		case SUCCESS:
			session.setAttribute("alertMessage","Article Selected for review");
			session.setAttribute("alertType","success" );
			response.sendRedirect("ReviewerArticlesForReview");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Review not created.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ReviewerArticlesForReview");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ReviewerArticlesForReview");
			break;
		}
		System.out.println("here");
		if(rc.isSessionReady())
			rc.endSession();
			return;
		
	}
}
