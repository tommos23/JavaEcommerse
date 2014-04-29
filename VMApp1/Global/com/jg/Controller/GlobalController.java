package com.jg.Controller;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Global;

public class GlobalController extends Controller{

	@SuppressWarnings("unchecked")
	public Global getGlobal()
	{
		List<Global> Global = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Global.class);
			Global = cr.list();
			session.getTransaction().commit();
			if (Global.size() > 0) {
				return Global.get(0);
			}
			else {
				return null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}
	
	public Global get(int id) {
		List<Global> global = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Global.class);
			cr.add(Restrictions.eq("id", id));
			global = cr.list();
			session.getTransaction().commit();
			if (global.size() > 0) {
				return global.get(0);
			}
			else {
				return null;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}

	public entryResponse update(int id, String name, String goals, String submissionGuidelines){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				global.setName(name);
				global.setGoals(goals);
				global.setSubmission_guidelines(submissionGuidelines);
				session.update(global);
				session.getTransaction().commit();
				return entryResponse.SUCCESS;
			}
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

	private entryResponse isExist(int id){
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Global.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				global = (Global)results.get(0);
				return entryResponse.EXIST;
			}
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
	Global global;

}