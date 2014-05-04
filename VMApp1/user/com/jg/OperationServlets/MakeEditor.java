package com.jg.OperationServlets;
import com.jg.Controller.*;
import com.jg.Model.Role;
import com.jg.Model.User;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserRegister
 */
public class MakeEditor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MakeEditor() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		RoleController rc = new RoleController();
		rc.startSession();
		Role role = rc.get("editor");
		if(rc.isSessionReady())
			rc.endSession();
		if (role == null) {
			session.setAttribute("alertMessage","<Strong>Sorry!</strong> Role not found.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ViewUsers");
		}
		UserController uc = new UserController();
		uc.startSession();
		User user = uc.get(Integer.parseInt((String) request.getParameter("id")));
		if (user == null) {
			session.setAttribute("alertMessage","<Strong>Sorry!</strong> User not found.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("ViewUsers");
		}
		switch(uc.changeRole(user, role)){
		case SUCCESS:
			String alertMessage =  "<Strong>Congratulations!</strong> You have successfully made a new editor.";
			String alertType =  "success";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("ViewUsers");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			alertMessage = "<Strong>Oops!!</strong> Something went wrong.";
			alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("welcome");
			break;
		}
		if(uc.isSessionReady())
			uc.endSession();
	}

}
