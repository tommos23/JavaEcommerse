package com.jg.ViewServlets;

import java.io.IOException;
import java.util.Date;
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

import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
public class ViewUnpostedLetters extends VelocityViewServlet {
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		//------Code to display alert message------
		if(session.getAttribute("alertMessage")!=null){
			context.put("alertMessage",session.getAttribute("alertMessage").toString());
			context.put("showAlert", "true");
			if(session.getAttribute("alertType") != null)
				context.put("alertType",session.getAttribute("alertType").toString());
			else
				context.put("alertType", "info");
			session.removeAttribute("alertMessage");
			session.removeAttribute("alertType");
		}
		//-----End of Alert Message Code---------
		/* get the template */
		SessionFactory factory;
		
		try{
	         factory = new Configuration().configure().buildSessionFactory();
	    }catch (Throwable ex) { 
	         System.err.println("Failed to create sessionFactory object." + ex);
	         throw new ExceptionInInitializerError(ex); 
	    }
		
		String id = request.getParameter("id");
		if (id != null) {
			Session session3 = factory.openSession();
		    Transaction tx = null;
		    try{
		        tx = session3.beginTransaction();
		        List letters = session3.createQuery("FROM LetterToEditor WHERE status = 0 AND id = "+id).list();
		        if (letters.size() > 0) {
		        	LetterToEditor letter = (LetterToEditor) letters.get(0);
		        	letter.setStatus(1);
		        	if (request.getParameter("editedText") != null && !request.getParameter("editedText").equals("")) {
		        		letter.setEdited_text(request.getParameter("editedText"));
		        		letter.setEdited_at(new Date());
			        	session3.save(letter);
			        	context.put("alertType", "info");
			        	context.put("alertMessage","Letter sent to author");
						context.put("showAlert", "true");
		        	}
		        	else {
			        	context.put("alertType", "danger");
			        	context.put("alertMessage","Please enter edited text before posting letter to author");
						context.put("showAlert", "true");
		        	}
		        }
		        else {
		        	context.put("alertType", "danger");
		        	context.put("alertMessage","Letter not sent to author");
					context.put("showAlert", "true");
		        }
		        tx.commit();
		    }catch (HibernateException e) {
		        if (tx!=null) tx.rollback();
		        e.printStackTrace(); 
		    }finally {
		    	session3.close(); 
		    }
		}
		
		Template template = null;
		Session session2 = factory.openSession();
	    Transaction tx = null;
	    try{
	        tx = session2.beginTransaction();
	        List letters = session2.createQuery("FROM LetterToEditor WHERE status = 0").list();
	        context.put("letters", letters);
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace();
	    }finally {
	        session2.close(); 
	    }
		context.put("application", "View Submitted Letters");
		try {
			template = getTemplate("submittedletters.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
