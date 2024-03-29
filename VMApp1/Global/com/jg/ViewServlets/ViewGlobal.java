package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.GlobalController;
import com.jg.Controller.UserController;
import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
public class ViewGlobal extends VelocityViewServlet {
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		User thisUser = null;
		try{
			uc.startSession();
			if (session.getAttribute("user_id") != null) {
				thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
				session.setAttribute("thisuser",thisUser);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(uc.isSessionReady())
				uc.endSession();
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher") || thisUser.getRole().getName().equals("activeauthor") || thisUser.getRole().getName().equals("passiveauthor"))) {
			if (session.isNew()){
				session.setAttribute("user", "false");
				System.out.println("false");
			}
			else if(session.getAttribute("user") == null){
				session.setAttribute("user", "false");
			}
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
		GlobalController gc = new GlobalController();			
		try {
			gc.startSession();
			context.put("global", gc.getGlobal());				
			gc.endSession();
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}finally{
			if(gc.isSessionReady())
				gc.endSession();
		}

		/* get the template */
		Template template = null;
		template = getTemplate("global/viewglobal.vm");
		return template; 
	}

}
