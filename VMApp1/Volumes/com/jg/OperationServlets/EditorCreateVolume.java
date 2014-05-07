package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.User;

/**
 * Servlet implementation class UserValidate
 */
public class EditorCreateVolume extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditorCreateVolume() {
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
			String description = request.getParameter("description");
			if (description == null || description.equals("")){
				session.setAttribute("alertMessage","Please write a description.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorNewVolume");
			}
			VolumeController vc = new VolumeController();
			vc.startSession();
			switch(vc.create(description)){
			case SUCCESS:
				session.setAttribute("alertMessage","Volume Created.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("EditorViewVolumes");
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Volume not created.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorViewVolumes");
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("EditorViewVolumes");
				break;
			}
			if (uc.isSessionReady())
				uc.endSession();
			if(vc.isSessionReady())
				vc.endSession();
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
