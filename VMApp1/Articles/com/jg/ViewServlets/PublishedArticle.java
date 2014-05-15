package com.jg.ViewServlets;

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
public class PublishedArticle extends VelocityViewServlet {
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
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		try {
			int id = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			ac.startSession();
			Article article = ac.get(id);
			ac.endSession();
			context.put("article", article);
			session.setAttribute("thisuser", thisUser);
			if (uc.isSessionReady())
				uc.endSession();
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}finally{
			if (uc.isSessionReady())
				uc.endSession();
			if (ac.isSessionReady())
				ac.endSession();
		}
		/* get the template */
		Template template = null;
		template = getTemplate("articles/publishedarticle.vm"); 
		return template;
	}

}
