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
import com.jg.Model.User;

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
		Template template = null;
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && thisUser.getRole().getName().equals("activeauthor")) {
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
		return template;
	}

}
