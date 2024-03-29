package com.jg.ViewServlets;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;


public class Welcome extends VelocityViewServlet 
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
		//------Code to display alert message------
		if(session.getAttribute("alertMessage")!=null){
			context.put("alertMessage",session.getAttribute("alertMessage").toString());
			context.put("showAlert", "true");
			if(session.getAttribute("alertType") != null)
				context.put("alertType",session.getAttribute("alertType").toString());
			else
				context.put("alertType", "info");
			session.removeAttribute("alertMessage");
			session.removeAttribute("alertType");
		}
		//-----End of Alert Message Code---------
		/* get the template */
		Template template = null;
		if (!session.isNew()){
			if(session.getAttribute("thisuser") != null){
				session.setAttribute("user", "true");
					try {
						response.sendRedirect("home");
					} catch (IOException e) {
						e.printStackTrace();
					}
				
			}
			else{
				session.setAttribute("user", "false");
			}
		}
		else{
			session.setAttribute("user", "false");
		}
		session.setAttribute("application", "JAMR - Online Journal");
		try {
			template = getTemplate("index.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}
}
