package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ArticleController;
import com.jg.Controller.LettersToEditorsController;
import com.jg.Controller.UserController;
import com.jg.Model.Article;
import com.jg.Model.User;

/**
 * Servlet implementation class UserValidate
 */
public class CreateLetter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateLetter() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("reader") || thisUser.getRole().getName().equals("author") || thisUser.getRole().getName().equals("reviewer"))) {
			int id = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			if (request.getParameter("text") == null) {
				session.setAttribute("alertMessage","Letter text is required.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewLetter?id="+id);
			}
			ArticleController ac = new ArticleController();
			ac.startSession();
			Article article = ac.get(id);
			if (article == null) {
				session.setAttribute("alertMessage","A valid article must be selected.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("home");
			}
			if (ac.isSessionReady())
				ac.endSession();
			LettersToEditorsController ltec = new LettersToEditorsController();
			ltec.startSession();
			switch(ltec.create(article, thisUser, request.getParameter("text"))){
			case SUCCESS:
				session.setAttribute("alertMessage","Letter has been sent to the editors for approval and response.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("home");
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Something went wrong. Try Again.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewLetter");
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewLetter");
				break;
			}
			if (uc.isSessionReady())
				uc.endSession();
			if(ltec.isSessionReady())
				ltec.endSession();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
