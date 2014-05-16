package com.jg.ViewServlets;

import java.io.IOException;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.UserController;
import com.jg.Model.User;
import com.jg.Model.Article;


/**
 * Servlet implementation class PublishedArticle
 */
public class ReviewerArticlesForReview extends VelocityViewServlet 
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
			UserController uc = new UserController();
			User thisUser = null;
			try{
			uc.startSession();
			if (session.getAttribute("user_id") != null) {
				thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
				session.setAttribute("thisuser", thisUser);
			}
			}
			catch(Exception e){
				e.printStackTrace();
			}
			finally{
				if (uc.isSessionReady())
					uc.endSession();
			}
			if (thisUser != null && (thisUser.getRole().getName().equals("activeauthor") || thisUser.getRole().getName().equals("passiveauthor"))) {
				int id = Integer.parseInt(session.getAttribute("user_id").toString());
				/* get the template */
				ArticleController ac = new ArticleController();
				ac.startSession();
				context.put("updated_articles",new HashSet<Article>(ac.getReviewerUpdatedArticles(id)));
				context.put("reviewing_articles",new HashSet<Article>( ac.getAllArticlesReviewerReviewing(id)));
				context.put("reviewed_articles",new HashSet<Article>( ac.getAllArticlesReviewerReviewed(id)));
				context.put("articles", new HashSet<Article>(ac.getAllArticlesForReviewerReview(id)));
				ac.endSession();
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
		try {
			template = getTemplate("articles/ReviewerArticlesForReview.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
