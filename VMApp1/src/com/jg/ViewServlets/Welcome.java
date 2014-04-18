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
			if(session.getAttribute("user") != null){
				if(session.getAttribute("user").equals("true")){
					try {
						response.sendRedirect("home");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(session.getAttribute("fname")!=null){
					context.put("fname", session.getAttribute("fname").toString());					
					session.removeAttribute("fname");
				}
				if(session.getAttribute("lname")!=null){
					context.put("lname", session.getAttribute("lname").toString());
					session.removeAttribute("lname");
				}
				if(session.getAttribute("email")!=null){
					context.put("email", session.getAttribute("email").toString());
					session.removeAttribute("email");
				}
				if(session.getAttribute("existemail")!=null){
					context.put("existemail", session.getAttribute("existemail").toString());
					session.removeAttribute("existemail");
				}
			}
			else{
				session.setAttribute("user", "false");
			}
		}
		else{
			session.setAttribute("user", "false");
		}

		context.put("application", "Login Application");
		try {
			template = getTemplate("index.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}
}
