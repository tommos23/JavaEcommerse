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

/**
 * Servlet implementation class PublishedArticle
 */
public class ReviewerEditReview extends VelocityViewServlet 
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
			session.setAttribute("alertMessage", "Review selected");
			session.setAttribute("alertType", "danger");
			try {
				response.sendRedirect("EditorArticlesForReview");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* get the template */
		Template template = null;
		boolean user = false;		
		if(session.getAttribute("user") != null){
			if(session.getAttribute("user").equals("true")){
				user = true;
			}
		}
		else{
			session.setAttribute("user", "false");
		}

		if (!user){
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				template = getTemplate("reviews/ReviwerEditReview.vm"); 
				ArticleController ac = new ArticleController();
				ac.startSession();
				context.put("articles", ac.get(Integer.parseInt(id)));
				context.put("article_id", id);
				ac.endSession();
				ReviewController rc = new ReviewController();
				rc.startSession();
				int user_id = Integer.parseInt(session.getAttribute("user_id").toString());
				System.out.println("USERid= " + user_id);
				System.out.println("articleID= " + id);
				context.put("review",rc.getUserReviewForArticle(Integer.parseInt(id),user_id));
				rc.endSession();
			} catch(Exception e ) {
				System.out.println("Error " + e);
			}
		}
		return template;
	}

}
