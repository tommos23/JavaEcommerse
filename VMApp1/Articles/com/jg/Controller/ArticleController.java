package com.jg.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Article;
import com.jg.Model.Keyword;
import com.jg.Model.Subject;
import com.jg.Model.User;
import com.jg.Model.Version;

public class ArticleController extends Controller{

	public entryResponse addNewArticle(String title,String abs,String keywords,String filepath,String email){
		String[] words = new String[255];
		System.out.println(keywords);
		if(keywords.contains(" "))
			words = keywords.split(" ");
		else
			words[0] = keywords;
		Set<Keyword> tempkey = new HashSet<Keyword>(0);
		for(String word : words){
			if(word != null)
				tempkey.add(new Keyword(word));
		}
		try{
			//Check if session factory is ready or not
			if(!isSessionReady()) throw new Exception();

			//Start Session
			session = sessionFactory.openSession();				
			session.beginTransaction();

			//Create an Group Object

			//Create an object of article
			Article a = new Article();
			a.setCreated_at(new Date());
			a.setMainAuthor(new UserController().getUser(email)); // Requires a user object before calling this
			a.setStatus(0);

			//Add objects of keywords into article
			//if(!tempkey.isEmpty())
				//a.setKeywords(tempkey);

			//Create an object of version of article
			Version ver = new Version();

			ver.setCreated_at(a.getCreated_at());
			ver.setTitle(title);
			ver.setAbs(abs);
			ver.setUrl(filepath);

			//add reference of article to version
			ver.setArticle(a);

			//Add objects of subject to it
			Set<Subject> sub = new HashSet<Subject>(0);
			sub.add(new Subject("Computer Science"));
			sub.add(new Subject("Information Technology"));
			ver.setSubjects(sub);

			session.saveOrUpdate(ver);
			session.getTransaction().commit();
			return entryResponse.SUCCESS;
		}
		catch(Exception e){
			e.printStackTrace();
			return entryResponse.DB_ERROR;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<Article> getAllArticles(int status)
	{
		List<Article> articles = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("status", status));
			articles = cr.list();
			session.getTransaction().commit();
			return articles;
		}
		catch(Exception e){
			e.printStackTrace();
			return articles;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}

	public entryResponse approveUpload(int id){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				article.setStatus(1);
				session.update(article);
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
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				article = (Article)results.get(0);
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
	Article article;

}