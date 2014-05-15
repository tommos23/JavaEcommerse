package com.jg.ViewServlets;

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
 * Servlet implementation class PublishedArticle
 */
public class ViewDocumentTemplates extends VelocityViewServlet 
{
	/**
	 * 
	 */
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

		/* get the template */
		Template template = null;
		UserController uc = new UserController();
		DocumentTemplateController vc = new DocumentTemplateController();
		uc.startSession();
		User thisUser = null;
		try{
			if (session.getAttribute("user_id") != null) 
				thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
			session.setAttribute("thisuser", thisUser);
			vc.startSession();
			context.put("templates", vc.getAllTemplates());
			vc.endSession();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (uc.isSessionReady())
				uc.endSession();
			if (vc.isSessionReady())
				vc.endSession();
		}
		try {
			template = getTemplate("documenttemplate/viewtemplates.vm");
		} 
		catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
