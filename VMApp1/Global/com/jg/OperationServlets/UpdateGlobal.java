package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.GlobalController;
import com.jg.Controller.UserController;
import com.jg.Model.User;

/**
 * Servlet implementation class UserValidate
 */
public class UpdateGlobal extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateGlobal() {
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
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			int id = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			else {
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewGlobal");
			}
			String name = request.getParameter("name");
			if (name == null || name.equals("")) {
				session.setAttribute("alertMessage","Please include global name.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditGlobal");
			}
			String submissionGuidelines = request.getParameter("submission_guidelines");
			if (submissionGuidelines == null || submissionGuidelines.equals("")) {
				session.setAttribute("alertMessage","Please include global submission guidelines.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditGlobal");
			}
			String goals = request.getParameter("goals");
			if (goals == null || goals.equals("")) {
				session.setAttribute("alertMessage","Please include global goals.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditGlobal");
			}
			GlobalController gc = new GlobalController();
			gc.startSession();
			switch(gc.update(id, name, goals, submissionGuidelines)){
			case SUCCESS:
				session.setAttribute("alertMessage","Letter is successfully posted.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("ViewGlobal");
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check global number.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewGlobal");
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewGlobal");
				break;
			case EXIST:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","It exists...");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewGlobal");
				break;
			case NOT_EXIST:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","It doesn't exists...");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewGlobal");
				break;
			default:
				break;
			}
			if (uc.isSessionReady())
				uc.endSession();
			if(gc.isSessionReady())
				gc.endSession();
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
