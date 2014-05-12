package com.jg.ViewServlets;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.SubjectController;
import com.jg.Model.Article;

public class UploadVersion extends VelocityViewServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, HttpServletResponse response, Context context )
	{     
		HttpSession session = request.getSession(true);
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

		/* get the template */
		Template template = null;
		boolean user = false;
		if(session.getAttribute("user") != null){
			if(session.getAttribute("user").equals("true"))
				user = true;
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
		}
		else{
			ArticleController ac =new ArticleController();
			SubjectController sc = new SubjectController();
			int article_id;
			System.out.println(request.getAttribute("for"));
			if(request.getParameter("for") != null){
				article_id = Integer.parseInt(request.getParameter("for").toString());
				try{
					ac.startSession();
					Article a = ac.get(article_id);
					ac.endSession();

					if(a.getMainAuthor().getId() == Integer.parseInt(session.getAttribute("user_id").toString())){
						sc.startSession();
						context.put("subjects", sc.getAllSubjects());
						sc.endSession();
						context.put("article", a);
					}
					else{
						session.setAttribute("alertMessage","<Strong>Sorry!!</strong> You are not authorised to view this page.");
						session.setAttribute("alertType","danger" );
						try {
							response.sendRedirect("home");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if (ac.isSessionReady())
						ac.endSession();
					if (sc.isSessionReady())
						sc.endSession();
				}
			}
			else{
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> You are not authorised to view this page.");
				session.setAttribute("alertType","danger" );
				try {
					response.sendRedirect("home");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		try {
			template = getTemplate("articles/uploadversion.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}
}
