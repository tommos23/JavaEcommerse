package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;

/**
 * Servlet implementation class PublishedArticle
 */
public class PublishedArticle extends VelocityViewServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{
		Template template = null;
		try {
			template = getTemplate("articles/PublishedArticles.vm"); 
			ArticleController ac = new ArticleController();
			ac.startSession();
			context.put("articles", ac.getAllArticles());
			
			ac.endSession();
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
