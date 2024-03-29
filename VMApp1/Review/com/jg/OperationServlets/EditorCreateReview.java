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
public class EditorCreateReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditorCreateReview() {
		super();
	}
	
	@SuppressWarnings("incomplete-switch")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			ArticleController ac = new ArticleController();
			ac.startSession();
			Article article = ac.get(Integer.parseInt(request.getParameter("article_id")));
			if(ac.isSessionReady())
				ac.endSession();
			if (!uc.isSessionReady())
				uc.startSession();
			User reviewer = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
			if(uc.isSessionReady())
				uc.endSession();
			if (request.getParameter("contribution") != null && request.getParameter("critism") != null && article != null && reviewer != null && request.getParameter("expertise") != null && request.getParameter("position") != null) {
				ReviewController rc = new ReviewController();
				rc.startSession();
				switch(rc.create(request.getParameter("contribution"), request.getParameter("critism"), Integer.parseInt(request.getParameter("expertise")), Integer.parseInt(request.getParameter("position")), article, reviewer)){
				case SUCCESS:
					session.setAttribute("alertMessage","Review Created.");
					session.setAttribute("alertType","success" );
					response.sendRedirect("EditorArticlesForReview");
					break;
				case FAIL:
					session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Review not created.");
					session.setAttribute("alertType","danger" );
					response.sendRedirect("EditorArticlesForReview");
					break;
				case DB_ERROR:
					session.setAttribute("user","false");
					session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
					session.setAttribute("alertType","danger" );
					response.sendRedirect("EditorArticlesForReview");
					break;
				}
				if (uc.isSessionReady())
					uc.endSession();
				if(rc.isSessionReady())
					rc.endSession();
				return;
			}
			else {
				if (uc.isSessionReady())
					uc.endSession();
				session.setAttribute("alertMessage","<Strong>Review not created!</strong> All fields are required.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorArticlesForReview");
				return;
			}
		}
		else {
			if (uc.isSessionReady())
				uc.endSession();
			String alertMessage = "<Strong>Oops!!</strong> You do not have permission to do that.";
			String alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
