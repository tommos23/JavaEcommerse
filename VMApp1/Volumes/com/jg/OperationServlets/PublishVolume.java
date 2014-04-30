package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.VolumeController;

/**
 * Servlet implementation class UserValidate
 */
public class PublishVolume extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PublishVolume() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		int id = 0;
		if (request.getParameter("id") != null) 
			id = Integer.parseInt(request.getParameter("id"));
		VolumeController vc = new VolumeController();
		vc.startSession();
		switch(vc.publish(id)){
		case SUCCESS:
			session.setAttribute("alertMessage","Volume has been published.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("EditorViewVolumes");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check volume number.");
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
		if(vc.isSessionReady())
			vc.endSession();
	}
}
