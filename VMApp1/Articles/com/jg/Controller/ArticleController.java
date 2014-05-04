package com.jg.Controller;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.Review;
import com.jg.Model.User;

public class ArticleController extends Controller{

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
	
	public Article get(int id) {
		List<Article> article = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("id", id));
			article = cr.list();
			session.getTransaction().commit();
			if (article.size() > 0) {
				return article.get(0);
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
	
	public List<Article> getAllArticlesForEditorReview()
	{
		List<Article> articles = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("status", 1));
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MONTH, -6);
			Date date = cal.getTime();
			cr.add(Restrictions.le("created_at", date));
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
	
	public List<Article> getAllArticlesReviewerReviewing(int id)
	{
		List<Article> articles = new LinkedList<Article>();;
		try{
			if(!isSessionReady()) throw new Exception();
			List<Review> reviews = null;
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("status", 0));
			System.out.println("ID=="+id);
			cr.add(Restrictions.eq("reviewer.id", id));
			System.out.println("ID1=="+id);
			reviews = cr.list();
			System.out.println("ID2=="+id);
			session.getTransaction().commit();
			System.out.println("size=="+reviews.size());
			
			session = sessionFactory.openSession();				
			session.beginTransaction();
			System.out.println("REVIEWING 3");
			for(int i = 0; i < reviews.size(); i++){
				Review r = reviews.get(i);
				articles.add(r.getArticle());
			}
			session.getTransaction().commit();
			return articles;
			
		} catch(Exception e){
			e.printStackTrace();
			return articles;
		}
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Article> getAllArticlesForReviewerReview(int id)
	{
		List<Article> articles = new LinkedList<Article>();;
		try{
			if(!isSessionReady()) throw new Exception();
			List<Review> reviews = null;
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("status", 0));
			System.out.println("ID=="+id);
			cr.add(Restrictions.eq("reviewer.id", id));
			System.out.println("ID1=="+id);
			reviews = cr.list();
			System.out.println("ID2=="+id);
			session.getTransaction().commit();
			System.out.println("size=="+reviews.size());
			
			if(reviews.size() < 3){
				System.out.println("in here");
				session = sessionFactory.openSession();				
				session.beginTransaction();
				Criteria cr2 = session.createCriteria(Article.class);
				cr2.add(Restrictions.eq("status", 1));
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				cal.add(Calendar.MONTH, -6);
				Date date = cal.getTime();
				cr2.add(Restrictions.le("created_at", date));
				articles = cr2.list();
				if(reviews.size() == 0){		
					session.getTransaction().commit();		
				} else {
					for (int i = 0; i < reviews.size(); i++){
						Review r = reviews.get(i);
						for (int j = 0; j < reviews.size(); j++){
							if(r.getArticle().getId() == articles.get(j).getId()){
								articles.remove(j);
							}
						}
					}
				}
			} 
			return articles;
//				else {
//				session = sessionFactory.openSession();				
//				session.beginTransaction();
//				System.out.println("REVIEWING 3");
//				for(int i = 0; i < reviews.size(); i++){
//					Review r = reviews.get(i);
//					articles.add(r.getArticle());
//				}
//				session.getTransaction().commit();
//				return articles;
//			}
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
	
	public entryResponse publishArticle(int id, Edition edition){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				article.setStatus(5);
				article.setEdition(edition);
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