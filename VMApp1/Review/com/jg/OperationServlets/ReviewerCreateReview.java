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
import com.jg.Controller.VolumeController;
import com.jg.Model.Article;
import com.jg.Model.User;
import com.jg.Services.EmailService;

/**
 * Servlet implementation class UserValidate
 */
public class ReviewerCreateReview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewerCreateReview() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		if (request.getParameter("contribution") != null && request.getParameter("critism") != null && article != null && reviewer != null && request.getParameter("expertise") != null && request.getParameter("position") != null) {
			ReviewController rc = new ReviewController();
			rc.startSession();
			switch(rc.create(request.getParameter("contribution"), request.getParameter("critism"), Integer.parseInt(request.getParameter("expertise")), Integer.parseInt(request.getParameter("position")), article, reviewer)){
			case SUCCESS:
				session.setAttribute("alertMessage","Review Created.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("ReviewerArticlesForReview");
				
				EmailService es = new EmailService();
				try {

					//send email to author
					String sub = "Article Reviewed";
					String title = article.getLatestVersion().getTitle();
					String email = article.getMainAuthor().getEmail();
					String username = article.getMainAuthor().getFirstname() + " " + article.getMainAuthor().getSurname();
					String msg = "<html><body>Dear "+username+",<br><br> Your article "+ title + "<br><br>" +
							"has been reviewed, please log into the site to view this review and update your article accordingly <br>" +
							"Review:<br>Position: ";
					switch (Integer.parseInt(request.getParameter("position"))) {
					case 0:
						msg += "Champion";
						break;
					case 1:
						msg += "Favourable";
						break;
					case 2:
						msg += "Indifferent";
						break;
					case 3:
						msg += "Detractor";
						break;
					}
					msg += "<br>Contribution: "+request.getParameter("contribution")+"<br>";
					msg += "Criticism: "+request.getParameter("critism")+"<br>";
					msg += "Expertise: ";
					switch (Integer.parseInt(request.getParameter("expertise"))) {
					case 0:
						msg += "Expert";
						break;
					case 1:
						msg += "Knowledgable";
						break;
					case 2:
						msg += "Outsider";
						break;
					}
					msg += "<br>If necessary please update your submission and re-submit a new version.";
					es.sendEmail(email,sub,msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
			if (uc.isSessionReady())
				uc.endSession();
			if(rc.isSessionReady())
				rc.endSession();
			return;
		}
		else {
			if (uc.isSessionReady())
				uc.endSession();
			System.out.println("or here");
			session.setAttribute("alertMessage","<Strong>Review not created!</strong> All fields are required.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("EditorArticlesForReview");
			return;
		}
	}
}
