package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.DocumentTemplateController;

/**
 * Servlet implementation class UserValidate
 */
public class CreateDocumentTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateDocumentTemplate() {
		super();
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		String name = request.getParameter("name");
		if (name == null || name.equals("")){
			session.setAttribute("alertMessage","Please write a name.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplates");
		}
		String description = request.getParameter("description");
		if (description == null || description.equals("")){
			session.setAttribute("alertMessage","Please write a description.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplates");
		}
		String format = request.getParameter("format");
		if (format == null || format.equals("")){
			session.setAttribute("alertMessage","Please write a format.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplates");
		}
		String url = request.getParameter("url");
		if (url == null || url.equals("")){
			session.setAttribute("alertMessage","Please write a url.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplates");
		}
		DocumentTemplateController dc = new DocumentTemplateController();
		dc.startSession();
		switch(dc.create(name,description,format,url)){
		case SUCCESS:
			session.setAttribute("alertMessage","Volume Created.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("ViewDocumentTemplates");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Volume not created.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			break;
		}
		if(dc.isSessionReady())
			dc.endSession();
	}
}

