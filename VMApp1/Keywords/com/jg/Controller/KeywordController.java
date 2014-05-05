package com.jg.Controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Keyword;

public class KeywordController extends Controller{
	
	public boolean isExist(String key) throws Exception{
		try{				
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Keyword.class);
			cr.add(Restrictions.eq("keyword", key));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty())
				return true;
			else
				return false;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			throw new Exception();
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}
	
	public Keyword getKeyword(){
		return keyword;
	}
	
	Session session = null;
	private Keyword keyword;

}
