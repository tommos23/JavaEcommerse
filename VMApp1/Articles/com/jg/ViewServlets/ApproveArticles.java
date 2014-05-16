package com.jg.ViewServlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.UserController;
import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
public class ApproveArticles extends VelocityViewServlet {
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
		ArticleController ac = new ArticleController();
		User thisUser = null;
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
		if (thisUser != null) {
			if (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher")) {

				try {				
					ac.startSession();
					Set<Article> articles = new HashSet<Article>(ac.getAllArticles(0));
					context.put("articles", articles);
					session.setAttribute("thisuser", thisUser);

					ac.endSession();
				} catch(Exception e ) {
					System.out.println("Error " + e);
				}finally{
					if (ac.isSessionReady())
						ac.endSession();
				}
			}
			else {
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
		}
		else {
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
		/* get the template */
		Template template = null;
		template = getTemplate("articles/UploadedArticles.vm"); 
		return template;
	}

}
