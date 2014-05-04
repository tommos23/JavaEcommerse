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
public class SwapPublisher extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SwapPublisher() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null &&thisUser.getRole().getName().equals("publisher")) {
			RoleController rc = new RoleController();
			rc.startSession();
			Role pubRole = rc.get("publisher");
			if (pubRole == null) {
				session.setAttribute("alertMessage","<Strong>Sorry!</strong> Role not found.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewUsers");
			}
			Role edRole = rc.get("editor");
			if (edRole == null) {
				session.setAttribute("alertMessage","<Strong>Sorry!</strong> Role not found.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewUsers");
			}
			if(rc.isSessionReady())
				rc.endSession();
			if (!uc.isSessionReady())
				uc.startSession();
			User user = uc.get(Integer.parseInt((String) request.getParameter("id")));
			if (user == null) {
				session.setAttribute("alertMessage","<Strong>Sorry!</strong> User not found.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ViewUsers");
			}
			switch(uc.changeRole(user, pubRole)){
			case SUCCESS:
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				String alertMessage = "<Strong>Oops!!</strong> Something went wrong (roles remain unchanged).";
				String alertType = "danger";
				session.setAttribute("alertMessage",alertMessage);
				session.setAttribute("alertType",alertType );
				response.sendRedirect("welcome");
				break;
			}
			switch(uc.changeRole(thisUser, edRole)){
			case SUCCESS:
				String alertMessage =  "<Strong>Congratulations!</strong> You have successfully swapped the publisher role.";
				String alertType =  "success";
				session.setAttribute("alertMessage",alertMessage);
				session.setAttribute("alertType",alertType );
				response.sendRedirect("ViewUsers");
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				alertMessage = "<Strong>Oops!!</strong> Something went wrong (there are now two publishers).";
				alertType = "danger";
				session.setAttribute("alertMessage",alertMessage);
				session.setAttribute("alertType",alertType );
				response.sendRedirect("welcome");
				break;
			}
			if(uc.isSessionReady())
				uc.endSession();
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
