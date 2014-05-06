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
public class CreateReply extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateReply() {
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
		if (thisUser != null && (thisUser.getRole().getName().equals("activeauthor") || thisUser.getRole().getName().equals("passiveauthor"))) {
			int id = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			if (request.getParameter("replyText") == null) {
				session.setAttribute("alertMessage","Reply text is required.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewReply?id="+id);
			}
			LettersToEditorsController ltec = new LettersToEditorsController();
			ltec.startSession();
			switch(ltec.replyToLetter(id, request.getParameter("replyText"))){
			case SUCCESS:
				session.setAttribute("alertMessage","Letter reply sent to editors for approval and publishing.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("EditedLetters");
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Something went wrong. Try Again.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewReply?id="+id);
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("NewReply?id="+id);
				break;
			}
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
