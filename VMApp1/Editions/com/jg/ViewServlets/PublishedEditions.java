package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.EditionController;
import com.jg.Controller.UserController;
import com.jg.Model.User;

/**
 * Servlet implementation class PublishedArticle
 */
public class PublishedEditions extends VelocityViewServlet 
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
		UserController uc = new UserController();
		User thisUser = null;
		int id = 0;
		if (request.getParameter("id") != null){
			id = Integer.parseInt(request.getParameter("id"));
			try{
				uc.startSession();
				if (session.getAttribute("user_id") != null) {
					thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
				}
				uc.endSession();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if (uc.isSessionReady())
					uc.endSession();
			}
			if(thisUser != null)
				session.setAttribute("thisuser",thisUser);
		}
		else {
			String alertMessage = "<Strong>Oops!!</strong> Volume not found.";
			String alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		EditionController ec = new EditionController();
		try {
			ec.startSession();
			context.put("editions", ec.getEditionsForVolume(id));			
			ec.endSession();
			uc.startSession();
			session.setAttribute("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
			uc.endSession();

		} catch(Exception e ) {
			System.out.println("Error " + e);
		}finally{
			if (uc.isSessionReady()) {
				uc.endSession();
			}
			if (ec.isSessionReady()) {
				ec.endSession();
			}
		}
		/* get the template */
		Template template = null;
		template = getTemplate("editions/publishededitions.vm"); 
		return template;
	}

}
