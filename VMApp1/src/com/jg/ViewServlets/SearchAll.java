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
public class SearchAll extends VelocityViewServlet 
{
	/**
	 * 
	 */
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
		Template template = null;
		String searchString = request.getParameter("search");
		
		try {
			System.out.println("Search String: " + searchString);
			template = getTemplate("user/searchAll.vm"); 
			ArticleController ac = new ArticleController();
			ac.startSession();
			context.put("searchString", searchString);
			context.put("articleSearch", ac.searchArticles(searchString));
			context.put("authorSearch", ac.searchAutohrs(searchString));
			context.put("keywordSearch", ac.searchKeywords(searchString));
			ac.endSession();
			

		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
