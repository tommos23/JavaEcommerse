package com.jg.ViewServlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class ViewUsers extends VelocityViewServlet 
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
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
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
			if (!uc.isSessionReady()) {
				uc.startSession();
			}
			List<User> users = uc.getUsers();
			context.put("users", users);
			context.put("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
	//		context.put("userid", session.getAttribute("user_id"));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Template template = null;
		template = getTemplate("user/viewusers.vm");
		return template;
	}

}
