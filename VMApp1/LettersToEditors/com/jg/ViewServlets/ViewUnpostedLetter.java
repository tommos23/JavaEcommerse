package com.jg.ViewServlets;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jg.Controller.LettersToEditorsController;
import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
public class ViewUnpostedLetter extends VelocityViewServlet {
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
		if (id == null) {
			session.setAttribute("alertMessage", "danger");
			session.setAttribute("alertType", "Letter not found");
			try {
				response.sendRedirect("UnpostedLetters");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		/* get the template */
		Template template = null;
		try {
			template = getTemplate("letters/submittedletter.vm"); 
			LettersToEditorsController ltec = new LettersToEditorsController();
			ltec.startSession();
			context.put("letter", ltec.get(Integer.parseInt(id)));
			
			ltec.endSession();
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
