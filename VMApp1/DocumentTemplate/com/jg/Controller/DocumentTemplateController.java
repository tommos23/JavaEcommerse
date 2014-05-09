package com.jg.Controller;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Template;



public class DocumentTemplateController extends Controller{

	@SuppressWarnings("unchecked")
	public List<Template> getAllTemplates()
	{
		List<Template> template = null;
		try{
			if(!isSessionReady()) throw new Exception();
			HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Template.class);
			template = cr.list();
			session.getTransaction().commit();
			return template;
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
	
	public Template get(int id) {
		List<Template> template = null;
		try{
			if(!isSessionReady()) throw new Exception();
			HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Template.class);
			cr.add(Restrictions.eq("id", id));
			template = cr.list();
			System.out.println("get");
			session.getTransaction().commit();
			if (template.size() > 0) {
				return template.get(0);
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
	
	public entryResponse create(String name, String description, String format, String url){
		try{
			if(!isSessionReady()) throw new Exception();
			System.out.println("NAME==" + name);
			System.out.println("description==" + description);
			System.out.println("format==" + format);
			System.out.println("url==" + url);
			HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();		
			template = new Template(name, description, format, url);
			session.save(template);
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

	public entryResponse update(int id, String name, String description, String format, String url){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
//				System.out.println("ID==" + id);
//				System.out.println("NAME==" + name);
//				System.out.println("description==" + description);
//				System.out.println("format==" + format);
//				System.out.println("url==" + url);
				HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				template.setName(name);
				template.setDescription(description);
				template.setFormat(format);
				template.setUrl(url);
				session.update(template);
				session.getTransaction().commit();
				return entryResponse.SUCCESS;
			} else
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
			HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Template.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				template = (Template)results.get(0);
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
	Template template;

}
