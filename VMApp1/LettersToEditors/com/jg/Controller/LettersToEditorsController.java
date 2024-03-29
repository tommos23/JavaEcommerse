package com.jg.Controller;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.LetterToEditor;
import com.jg.Model.User;

@SuppressWarnings({"unchecked" ,"rawtypes"})
public class LettersToEditorsController extends Controller{

	public List<LetterToEditor> getAllLettersToEditors(int status)
	{
		List<LetterToEditor> LetterToEditor = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(LetterToEditor.class);
			cr.add(Restrictions.eq("status", status));
			LetterToEditor = cr.list();
			session.getTransaction().commit();
			return LetterToEditor;
		}
		catch(Exception e){
			e.printStackTrace();
			return LetterToEditor;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}
	
	public List<LetterToEditor> getLettersForEdition(Edition edition)
	{
		List<LetterToEditor> LetterToEditor = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(LetterToEditor.class);
			cr.add(Restrictions.eq("publish_edition", edition.getId()));
			LetterToEditor = cr.list();
			session.getTransaction().commit();
			return LetterToEditor;
		}
		catch(Exception e){
			e.printStackTrace();
			return LetterToEditor;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}
	
	public LetterToEditor get(int id) {
		List<LetterToEditor> LetterToEditor = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(LetterToEditor.class);
			cr.add(Restrictions.eq("id", id));
			LetterToEditor = cr.list();
			session.getTransaction().commit();
			if (LetterToEditor.size() > 0) {
				return LetterToEditor.get(0);
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

	public entryResponse postLetter(int id, String editedText){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				letter.setStatus(1);
				letter.setEdited_text(editedText);
				letter.setEdited_at(new Date());
				session.update(letter);
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
	
	public entryResponse approveLetter(int id) {
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				letter.setStatus(3);
				session.update(letter);
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
	
	public entryResponse publishLetter(int id, Edition edition){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				letter.setStatus(4);
				letter.setPublish_edition(edition.getId());
				session.update(letter);
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
	
	public entryResponse create(Article article, User user, String text){
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();		
			letter = new LetterToEditor();
			letter.setArticle(article);
			letter.setUser(user);
			letter.setCreated_at(new Date());
			letter.setText(text);
			letter.setPublish_edition(0);
			letter.setStatus(0);
			session.save(letter);
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
	
	public entryResponse replyToLetter(int id, String replyText) {
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				letter.setStatus(2);
				letter.setReply_text(replyText);
				letter.setReplyed_at(new Date());
				session.update(letter);
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
			Criteria cr = session.createCriteria(LetterToEditor.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				letter = (LetterToEditor)results.get(0);
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
	LetterToEditor letter;

}