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
		
		Template template = null;
		Session session2 = factory.openSession();
	    Transaction tx = null;
	    try{
	        tx = session2.beginTransaction();
	        String id = request.getParameter("id");
	        if (id != null && !id.equals("")) {
			    List letters = session2.createQuery("FROM LetterToEditor WHERE status = 0 AND id = "+id).list();
			    if (letters.size() > 0) {
			        LetterToEditor letter = (LetterToEditor) letters.get(0);
			        context.put("letter", letter);
			        tx.commit();
			    }
		        else {
		        	session.setAttribute("alertType", "danger");
		        	session.setAttribute("alertMessage", "Letter not found");
		        	response.sendRedirect("ViewUnpostedLetters");
		        }
	        }
	        else {
	        	session.setAttribute("alertType", "danger");
	        	session.setAttribute("alertMessage", "Letter not found");
	        	response.sendRedirect("ViewUnpostedLetters");
	        }
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace();
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
	        session2.close(); 
	    }
		context.put("application", "Submitted Letter");
		try {
			template = getTemplate("submittedletter.vm");
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
