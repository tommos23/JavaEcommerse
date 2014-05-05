package com.jg.Controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Article;
import com.jg.Model.Keyword;

public class KeywordController extends Controller{
	
	public entryResponse isExist(String key){
		try{				
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("keyword", key));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty())
				return entryResponse.EXIST;
			else
				return entryResponse.NOT_EXIST;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			return entryResponse.DB_ERROR;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}
	
	Session session = null;
	private Keyword keyword;

}
