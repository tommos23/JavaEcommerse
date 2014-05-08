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
public class ReviewerViewArticle extends VelocityViewServlet 
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
		
		String article_id = request.getParameter("id");
		if (article_id == null || article_id.equals("")) {
			session.setAttribute("alertMessage", "No article selected");
			session.setAttribute("alertType", "danger");
			try {
				response.sendRedirect("ReviewerArticlesForReview");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Template template = null;
		boolean user = false;		
		if(session.getAttribute("user") != null){
			if(session.getAttribute("user").equals("true"))
				user = true;
		}
		if (!user){
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			int id = Integer.parseInt(session.getAttribute("user_id").toString());
			/* get the template */
			ArticleController ac = new ArticleController();
			ac.startSession();
			context.put("article", ac.get(Integer.parseInt(article_id)));
			context.put("user_id",id);
			ac.endSession();					
		}
		try {
			template = getTemplate("articles/ReviewerViewArticle.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
