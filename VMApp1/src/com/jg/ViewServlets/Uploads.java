package com.jg.ViewServlets;


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
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.jg.Model.*;

/**
 * Servlet implementation class Uploads
 */
@SuppressWarnings({ "rawtypes" })
public class Uploads extends VelocityViewServlet {
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("deprecation")
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
		        List articles = session3.createQuery("FROM Article WHERE status = 0 AND id = "+id).list();
		        if (articles.size() > 0) {
		        	Article article = (Article) articles.get(0);
		        	article.setStatus(1);
		        	session3.save(article);
		        	context.put("alertType", "info");
		        	context.put("alertMessage","Article set for review");
					context.put("showAlert", "true");
		        }
		        else {
		        	context.put("alertType", "danger");
		        	context.put("alertMessage","Article not set for review");
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
	        List articles = session2.createQuery("FROM Article WHERE status = 0").list();
	        context.put("articles", articles);
	        tx.commit();
	    }catch (HibernateException e) {
	        if (tx!=null) tx.rollback();
	        e.printStackTrace(); 
	    }finally {
	        session2.close(); 
	    }
		context.put("application", "View Uploads");
		try {
			template = getTemplate("uploads.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}

}
