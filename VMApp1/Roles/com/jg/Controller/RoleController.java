package com.jg.Controller;

import java.util.Date;
import java.util.List;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.*;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class RoleController extends Controller{

	public entryResponse isExist(String name){
		try{				
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Role.class);
			cr.add(Restrictions.eq("name", name));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				role = (Role)results.get(0);
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

	public Role getRole() {
		return role;
	}
	
	public Role get(int id) {
		List<Role> role = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Role.class);
			cr.add(Restrictions.eq("id", id));
			role = cr.list();
			session.getTransaction().commit();
			if (role.size() > 0) {
				return role.get(0);
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
	
	public Role get(String name) {
		List<Role> role = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Role.class);
			cr.add(Restrictions.eq("name", name));
			role = cr.list();
			session.getTransaction().commit();
			if (role.size() > 0) {
				return role.get(0);
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
	
	public List<Role> getRoles() {
		List<Role> roles = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Role.class);
			roles = cr.list();
			session.getTransaction().commit();
			return roles;
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

	private Role role;
	Session session = null;
}
