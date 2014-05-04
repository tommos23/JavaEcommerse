package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.EditionController;
import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.Edition;
import com.jg.Model.User;

/**
 * Servlet implementation class UserValidate
 */
public class PublishEdition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishEdition() {
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
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			int id = 0;
			int status = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			if (request.getParameter("status") != null) 
				status = Integer.parseInt(request.getParameter("status"));
			EditionController ec = new EditionController();
			ec.startSession();
			Edition edition = ec.get(id);
			switch(ec.publish(id, Integer.parseInt(request.getParameter("status")))){
			case SUCCESS:
				session.setAttribute("alertMessage","Edition status changed.");
				session.setAttribute("alertType","success");
				response.sendRedirect("EditorViewEditions?id="+edition.getVolume().getId());
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check volume number.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorViewEditions?id="+edition.getId());
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorViewEditions?id="+edition.getId());
				break;
			}
			if(ec.isSessionReady())
				ec.endSession();
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
