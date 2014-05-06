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
import com.jg.Controller.VolumeController;
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
		uc.startSession();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			EditionController ec = new EditionController();
			ec.startSession();
			Edition edition = ec.get(id);
			context.put("edition", edition);
			ec.endSession();
			ArticleController ac = new ArticleController();
			ac.startSession();
			context.put("articles", ac.getArticlesForEdition(edition));
			ac.endSession();
			LettersToEditorsController ltec = new LettersToEditorsController();
			ltec.startSession();
			context.put("letters", ltec.getLettersForEdition(edition));
			ltec.endSession();
			context.put("thisuser", uc.get(Integer.parseInt(session.getAttribute("user_id").toString())));
			
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		if (uc.isSessionReady()) {
			uc.endSession();
		}
		/* get the template */
		Template template = null;
		template = getTemplate("editions/publishededitioncontent.vm"); 
		return template;
	}

}
