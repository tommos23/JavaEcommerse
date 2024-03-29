package com.jg.ViewServlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

import com.jg.Controller.ArticleController;
import com.jg.Controller.EditionController;
import com.jg.Controller.LettersToEditorsController;
import com.jg.Controller.UserController;
import com.jg.Model.Edition;
import com.jg.Model.User;

/**
 * Servlet implementation class PublishedArticle
 */
public class PublishedEditionContent extends VelocityViewServlet 
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
		UserController uc = new UserController();
		User thisUser = null;
		int id = 0;
		if (request.getParameter("id") != null)
			id = Integer.parseInt(request.getParameter("id"));
		else {
			String alertMessage = "<Strong>Oops!!</strong> Edition not found.";
			String alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try{
			uc.startSession();
			if (session.getAttribute("user_id") != null) {
				thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
			}
			uc.endSession();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (uc.isSessionReady())
				uc.endSession();
		}
		if(thisUser != null)
			session.setAttribute("thisuser",thisUser);
		EditionController ec = new EditionController();
		ArticleController ac = new ArticleController();
		LettersToEditorsController ltec = new LettersToEditorsController();
		try {
			ec.startSession();
			Edition edition = ec.get(id);
			context.put("edition", edition);
			ec.endSession();
			ac.startSession();
			context.put("articles", ac.getArticlesForEdition(edition));
			ac.endSession();
			ltec.startSession();
			context.put("letters", ltec.getLettersForEdition(edition));
			ltec.endSession();
			uc.startSession();
			session.setAttribute("thisuser", uc.get(id));
			uc.endSession();
			
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		finally{
		if (uc.isSessionReady()) {
			uc.endSession();
		}
		if (ec.isSessionReady()) {
			ec.endSession();
		}
		if (ac.isSessionReady()) {
			ac.endSession();
		}
		if (ltec.isSessionReady()) {
			ltec.endSession();
		}
		
		}
		/* get the template */
		Template template = null;
		template = getTemplate("editions/publishededitioncontent.vm"); 
		return template;
	}

}
