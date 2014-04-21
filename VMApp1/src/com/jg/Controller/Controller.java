package com.jg.Controller;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

@SuppressWarnings("deprecation")
public class Controller {
	public void startSession(){
		try{
			sessionFactory = new AnnotationConfiguration().configure().buildSessionFactory();
			sessionReady = true;
		}catch (Throwable ex) { 
			sessionReady = false;
			System.err.println("Failed to create sessionFactory object." + ex);
		}
	}
	public void endSession() {
		if(sessionFactory != null)
			sessionFactory.close();
		sessionReady = false;
	}
	public boolean isSessionReady() {
		return sessionReady;
	}
	protected SessionFactory sessionFactory = null;
	private boolean sessionReady = false;
	
	public static enum validateResponse {VALID,INVALID,DB_ERROR}
	public static enum entryResponse {EXIST,NOT_EXIST,SUCCESS,DB_ERROR}
}
