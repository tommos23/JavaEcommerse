package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.LettersToEditorsController;

/**
 * Servlet implementation class UserValidate
 */
public class PublishLetter extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishLetter() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		int id = 0;
		if (request.getParameter("id") != null) 
			id = Integer.parseInt(request.getParameter("id"));
		LettersToEditorsController ltec = new LettersToEditorsController();
		ltec.startSession();
		switch(ltec.publishLetter(id)){
		case SUCCESS:
			session.setAttribute("alertMessage","Letter is successfully published.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("LettersWithReplies");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check letter number.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("LettersWithReplies");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("LettersWithReplies");
			break;
		}
		if(ltec.isSessionReady())
			ltec.endSession();
	}
}
