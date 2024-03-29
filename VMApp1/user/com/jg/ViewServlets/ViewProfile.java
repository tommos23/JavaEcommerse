package com.jg.ViewServlets;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.UserController;
import com.jg.Model.User;

public class ViewProfile extends VelocityViewServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{     
		HttpSession session = request.getSession(true);
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
		boolean user = false;
		if(session.getAttribute("user") != null){
			if(session.getAttribute("user").equals("true"))
				user = true;
		}
		else{
			session.setAttribute("user", "false");
		}

		if (!user){
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{		
			session.setAttribute("application", "JAMR - Online Journal");
			UserController uc = new UserController();
			ArticleController ac = new ArticleController();
			try{				
			uc.startSession();
			User thisuser = uc.getUser(session.getAttribute("user_email").toString());
			session.setAttribute("thisuser", thisuser);
			uc.endSession();
			ac.startSession();
			context.put("articles", ac.getAllArticlesForUser(thisuser.getId()));
			ac.endSession();
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if (uc.isSessionReady())
					uc.endSession();
			}
		}		
		try {
			template = getTemplate("user/profile.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		
		return template;
	}
}
