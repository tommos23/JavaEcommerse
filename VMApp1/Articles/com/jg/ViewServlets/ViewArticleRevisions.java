package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.ReviewController;
import com.jg.Controller.UserController;
import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
public class ViewArticleRevisions extends VelocityViewServlet {
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, HttpServletResponse response, Context context )
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
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			try {
				
				int article_id = -1;
				article_id = Integer.parseInt(request.getParameter("article_id"));
				if(article_id != -1){
					ArticleController ac = new ArticleController();
					ac.startSession();
					context.put("versions", ac.getAllVersionsForArticle(article_id));
					Article art = ac.get(article_id);
					context.put("article", art);
					ac.endSession();
					ReviewController rc = new ReviewController();
					rc.startSession();
					context.put("reviews", rc.getReviewsForArticle(art));
					rc.endSession();
					session.setAttribute("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
				} else {
					String alertMessage = "No article selected";
					String alertType = "danger";
					session.setAttribute("alertMessage",alertMessage);
					session.setAttribute("alertType",alertType );
					try {
						response.sendRedirect("welcome");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			} catch(Exception e ) {
				System.out.println("Error " + e);
			}
			if (uc.isSessionReady())
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
				e.printStackTrace();
			}
		}
		/* get the template */
		Template template = null;
		template = getTemplate("articles/ArticleVersionHistory.vm"); 
		return template;
	}

}
