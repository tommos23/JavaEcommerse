package com.jg.Controller;


import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.Keyword;
import com.jg.Model.Review;
import com.jg.Model.Subject;
import com.jg.Model.Version;

public class ArticleController extends Controller{

	public entryResponse addNewArticle(String title,String abs,String keywords,Set<Integer> subIds, String filepath, String email){
		String[] words = new String[255];
		//System.out.println(keywords);
		if(keywords.contains(" "))
			words = keywords.split(" ");
		else
			words[0] = keywords;

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

			//Create an object of version of article
			Version ver = new Version();

			ver.setCreated_at(a.getCreated_at());
			ver.setTitle(title);
			ver.setAbs(abs);
			ver.setUrl(filepath);

			//add reference of article to version
			ver.setArticle(a);

			
		    session.save(ver);
			/*
			Set<Subject> sub = new HashSet<Subject>(0);
			sub.add(new Subject("Computer Science"));
			sub.add(new Subject("Information Technology"));
			ver.setSubjects(sub);
			 */

			//Add objects of keywords into article
			KeywordController kc = new KeywordController();
			kc.startSession();
			Set<Keyword> tempkey = new HashSet<Keyword>();
			kc.startSession();
			for(String word : words){
				if(word != null){
					if(kc.isExist(word)){
						Set<Article> tempset = new HashSet<Article>();
						tempset.addAll(kc.getKeyword().getArticles());
						System.out.println(a.getId());
						tempset.add(a);
						kc.getKeyword().setArticles(tempset);
						tempkey.add(kc.getKeyword());
					}						
					else{
						Set<Article> tempset = new HashSet<Article>();
						tempset.add(a);
						Keyword key = new Keyword(word);
						key.setArticles(tempset);
						tempkey.add(key);
					}
				}					
			}
			kc.endSession();
			a.setKeywords(tempkey);
			session.saveOrUpdate(a);
			
			
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

	public List<Version> getAllVersionsForArticle(int article_id){
		List<Version> versions = null;

		try{
			if(!isSessionReady()) throw new Exception();
			System.out.println("ARTICLE ID: " + article_id);
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Version.class);
			cr.add(Restrictions.eq("article.id", article_id));
			cr.addOrder(Order.desc("created_at"));
			versions = cr.list();
			session.getTransaction().commit();
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
		return versions;
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
//			Calendar cal = Calendar.getInstance();
//			cal.setTime(new Date());
//			cal.add(Calendar.MONTH, -6);
//			Date date = cal.getTime();
//			cr.add(Restrictions.le("created_at", date));
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

	public List<Article> getArticlesForEdition(Edition edition)
	{
		List<Article> articles = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();
			session.beginTransaction();
			Criteria cr = session.createCriteria(Article.class);
			cr.add(Restrictions.eq("edition", edition));
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
			cr.add(Restrictions.eq("position",-1));
			cr.add(Restrictions.eq("expertise",-1));
			System.out.println("ID1=="+id);
			reviews = cr.list();
			System.out.println("ID2=="+id);
			session.getTransaction().commit();
			System.out.println("size=="+reviews.size());

			session = sessionFactory.openSession();				
			session.beginTransaction();

			System.out.println("REVIEWING: " + reviews.size());
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

	public List<Article> getAllArticlesReviewerReviewed(int id)
	{
		List<Article> articles = new LinkedList<Article>();;
		try{
			if(!isSessionReady()) throw new Exception();
			List<Review> reviews = null;
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("status", 0));
			cr.add(Restrictions.eq("reviewer.id", id));
			cr.add(Restrictions.ne("position",-1));
			reviews = cr.list();
			session.getTransaction().commit();		
			session = sessionFactory.openSession();				
			session.beginTransaction();

			for(int i = 0; i < reviews.size(); i++){
				Review r = reviews.get(i);
				if(r.getVersion() == r.getArticle().getLatestVersion()) {
					articles.add(r.getArticle());
				}
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

	public List<Article> getReviewerUpdatedArticles(int id) {
		List<Article> articles = new LinkedList<Article>();;
		try{
			if(!isSessionReady()) throw new Exception();
			List<Review> reviews = null;
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("status", 0));
			cr.add(Restrictions.eq("reviewer.id", id));
			cr.add(Restrictions.ne("position",-1));
			reviews = cr.list();
			session.getTransaction().commit();		
			session = sessionFactory.openSession();				
			session.beginTransaction();

			for(int i = 0; i < reviews.size(); i++){
				Review r = reviews.get(i);
				if(r.getVersion() != r.getArticle().getLatestVersion()) {
					articles.add(r.getArticle());
				}
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
//				cal.setTime(new Date());
//				cal.add(Calendar.MONTH, -6);
//				Date date = cal.getTime();
//				cr2.add(Restrictions.le("created_at", date));
				articles = cr2.list();
				System.out.println("Article len: " + articles.size());
				if(reviews.size() == 0){		
					session.getTransaction().commit();		
				} else {
					for (int i = 0; i < reviews.size(); i++){
						Review r = reviews.get(i);			
						for (int j = 0; j < articles.size(); j++){
							System.out.println("Review ID: " + r.getArticle().getId());
							System.out.println("Article ID: " + articles.get(j).getId());
							if(r.getArticle().getId() == articles.get(j).getId()){
								articles.remove(j);
							}
						}
					}
					session.getTransaction().commit();
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