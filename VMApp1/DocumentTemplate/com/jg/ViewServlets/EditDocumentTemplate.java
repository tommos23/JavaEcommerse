package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.DocumentTemplateController;
import com.jg.Controller.UserController;
import com.jg.Model.User;

/**
 * Servlet implementation class Uploads
 */
public class EditDocumentTemplate extends VelocityViewServlet {
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		if (session.isNew()){
			session.setAttribute("user", "false");
			System.out.println("false");
		}
		else if(session.getAttribute("user") == null){
				session.setAttribute("user", "false");
		}
		//------Code to display alert message------
		if(session.getAttribute("alertMessage")!=null){
			context.put("alertMessage",session.getAttribute("alertMessage").toString());
			context.put("showAlert", "true");
			if(session.getAttribute("alertType") != null)
				context.put("alertType",session.getAttribute("alertType").toString());
			else
				context.put("alertType", "info");
			session.setAttribute("alertMessage", null);
			session.setAttribute("alertType", null);
		}
		//-----End of Alert Message Code---------
		
		String id = request.getParameter("id");
		if (id == null || id.equals("")) {
			session.setAttribute("alertMessage", "No docment template setected to be edited");
			session.setAttribute("alertType", "danger");
			try {
				response.sendRedirect("ViewDocumentTemplates");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* get the template */
		
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			try {
				DocumentTemplateController dtc = new DocumentTemplateController();
				dtc.startSession();
				context.put("template", dtc.get(Integer.parseInt(id)));
				context.put("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
				dtc.endSession();
			} catch(Exception e ) {
				System.out.println("Error " + e);
			}
			if (uc.isSessionReady())
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
				e.printStackTrace();
			}
		}
		Template template = null;
		template = getTemplate("documenttemplate/edittemplate.vm"); 
		return template;
	}

}
