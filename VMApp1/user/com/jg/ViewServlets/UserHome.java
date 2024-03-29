package com.jg.ViewServlets;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.UserController;
import com.jg.Model.User;

public class UserHome extends VelocityViewServlet 
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
		if(session.getAttribute("user") != null && session.getAttribute("thisuser") != null ){
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
			User thisUser = null;
			try{
				uc.startSession();
				if (session.getAttribute("user_id") != null) {
					thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				if (uc.isSessionReady())
					uc.endSession();
			}
			session.setAttribute("thisuser", thisUser);
		}		
		try {
			template = getTemplate("user/home.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}
}
