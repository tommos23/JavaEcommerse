package com.jg.Controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Edition;
import com.jg.Model.Volume;

public class EditionController extends Controller{

	@SuppressWarnings("unchecked")
	public List<Edition> getEditionsForVolume(int volume_id)
	{
		List<Edition> Edition = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Edition.class);
			cr.add(Restrictions.eq("volume.id", volume_id));
			Edition = cr.list();
			session.getTransaction().commit();
			return Edition;
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
	
	public entryResponse publish(int id, int status){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				edition.setStatus(status);
				session.update(edition);
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
	
	public Edition get(int id) {
		List<Edition> edition = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Edition.class);
			cr.add(Restrictions.eq("id", id));
			edition = cr.list();
			session.getTransaction().commit();
			if (edition.size() > 0) {
				return edition.get(0);
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
	
	public Edition get(String description, Volume volume) {
		List<Edition> edition = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Edition.class);
			cr.add(Restrictions.eq("description", description));
			cr.add(Restrictions.eq("volume", volume));
			edition = cr.list();
			session.getTransaction().commit();
			if (edition.size() > 0) {
				return edition.get(0);
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
	
	public entryResponse create(String description, Volume vol){
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			edition = new Edition(description, vol);
			session.save(edition);
			session.getTransaction().commit();
			return entryResponse.SUCCESS;
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

	public entryResponse update(int id, int status, String description, Volume volume){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				edition.setStatus(status);
				edition.setDescription(description);
				edition.setVolume(volume);
				session.update(edition);
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
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Edition.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				edition = (Edition)results.get(0);
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
	Edition edition;

}