package com.jg.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Subject;

@SuppressWarnings({"unchecked"})
public class SubjectController extends Controller {	
	@SuppressWarnings("rawtypes")
	public Set<Subject> getAllSubjects(){
		Set<Subject> subjects = new HashSet<Subject>(0);
		try{
			if(!isSessionReady()) throw new Exception();
			session =  HibernateUtil.getSessionFactory().getCurrentSession();		
			session.beginTransaction();
			Criteria cr = session.createCriteria(Subject.class);
			List resSubs = cr.list();
			subjects.addAll(resSubs);
			session.getTransaction().commit();
			return subjects;
		}
		catch(Exception e){
			e.printStackTrace();
			return subjects;
		}
		finally{
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	public Subject getSubject(int id){
		List<Subject> tempSubs;
		try{
			if(!isSessionReady()) throw new Exception();
			session =  HibernateUtil.getSessionFactory().getCurrentSession();			
			session.beginTransaction();
			Criteria cr = session.createCriteria(Subject.class);
			cr.add(Restrictions.eq("id", id));
			tempSubs = cr.list();
			session.getTransaction().commit();
			if (tempSubs.size() > 0) {
				return tempSubs.get(0);
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
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	
	private Session session = null;
}
