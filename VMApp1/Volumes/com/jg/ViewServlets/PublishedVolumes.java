package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.User;

/**
 * Servlet implementation class PublishedArticle
 */
public class PublishedVolumes extends VelocityViewServlet 
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
		uc.startSession();
		try {
			VolumeController vc = new VolumeController();
			vc.startSession();
			context.put("volumes", vc.getWithStatus(1));
			context.put("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
			
			vc.endSession();
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		if (uc.isSessionReady()) {
			uc.endSession();
		}
		/* get the template */
		Template template = null;
		template = getTemplate("volumes/publishedvolumes.vm"); 
		return template;
	}

}
