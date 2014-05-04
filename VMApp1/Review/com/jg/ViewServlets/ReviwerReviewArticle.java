package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;

/**
 * Servlet implementation class PublishedArticle
 */
public class ReviwerReviewArticle extends VelocityViewServlet 
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

		String id = request.getParameter("id");
		if (id == null || id.equals("")) {
			session.setAttribute("alertMessage", "No docment template setected to be edited");
			session.setAttribute("alertType", "danger");
			try {
				response.sendRedirect("ReviewerArticlesForReview");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* get the template */
		Template template = null;
		try {
			template = getTemplate("reviews/ReviwerReviewArticle.vm"); 
			ArticleController ac = new ArticleController();
			ac.startSession();
			context.put("articles", ac.get(Integer.parseInt(id)));
			context.put("article_id", id);
			ac.endSession();
			
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
