package com.jg.Controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Volume;

public class VolumeController extends Controller{

	@SuppressWarnings("unchecked")
	public List<Volume> getAllVolumes()
	{
		List<Volume> Volume = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Volume.class);
			Volume = cr.list();
			session.getTransaction().commit();
			return Volume;
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
	
	public Volume get(int id) {
		List<Volume> volume = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Volume.class);
			cr.add(Restrictions.eq("id", id));
			volume = cr.list();
			System.out.println("get");
			session.getTransaction().commit();
			if (volume.size() > 0) {
				return volume.get(0);
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
	
	public List<Volume> getWithStatus(int status) {
		List<Volume> volumes = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Volume.class);
			cr.add(Restrictions.eq("status", status));
			volumes = cr.list();
			session.getTransaction().commit();
			return volumes;
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
				volume.setStatus(status);
				session.update(volume);
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
	
	public entryResponse create(String description){
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();		
			volume = new Volume(description);
			session.save(volume);
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

	public entryResponse update(int id, int status, String description){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				volume.setStatus(status);
				volume.setDescription(description);
				session.update(volume);
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
			Criteria cr = session.createCriteria(Volume.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				volume = (Volume)results.get(0);
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
	Volume volume;

}