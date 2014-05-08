package com.jg.Controller;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.jg.Controller.Controller.entryResponse;
import com.jg.Model.Article;
import com.jg.Model.Edition;
import com.jg.Model.Review;
import com.jg.Model.Role;
import com.jg.Model.User;
import com.jg.Model.Volume;

public class ReviewController extends Controller{
	public Review get(int id) {
		List<Review> review = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("id", id));
			review = cr.list();
			System.out.println("get");
			session.getTransaction().commit();
			if (review.size() > 0) {
				return review.get(0);
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
	
	public List<Review> getReviewsForArticle(Article article) {
		List<Review> review = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("article", article));
			review = cr.list();
			System.out.println("get");
			session.getTransaction().commit();
			return review;
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
	
	public List<Review> getUserReviewForArticle(int article_id, int user_id) {
		List<Review> review = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("article.id", article_id));
			cr.add(Restrictions.eq("reviewer.id", user_id));
			review = cr.list();
			session.getTransaction().commit();
			return review;
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
	
	public entryResponse create(String contribution, String critism, int expertise, int position, Article article, User reviewer){
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			review = new Review();
			review.setContribution(contribution);
			review.setCritism(critism);
			review.setCreated_at(new Date());
			review.setStatus(0);
			review.setReviewer(reviewer);
			review.setExpertise(expertise);
			review.setArticle(article);
			review.setPosition(position);
			session.save(review);
			session.getTransaction().commit();
			
			checkArticle(article);
			
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
	
	public void checkArticle(Article article) {
		// get all reviews for articles
		List<Review> reviews = getReviewsForArticle(article);
		// count champions and detractors
		int champion = 0;
		int detractor = 0;
		int total = 0;
		for (int i = 0; i < reviews.size(); i++) {
			if (article.getLatestVersion().getId() == reviews.get(i).getVersion().getId()) {
				switch (reviews.get(i).getPosition()) {
				case 0:
					champion++;
					break;
				case 3:
					detractor++;
					break;
				}
				total++;
			}
		}
		// check if reviews meet any rules
		// update article accordingly
		ArticleController ac = new ArticleController();
		ac.startSession();
		if (champion >= 2 && detractor == 0 && total >= 3) {
			ac.updateStatus(article.getId(), 3);
		}
		else if (detractor >= 2 && champion == 0 && total >= 3) {
			ac.updateStatus(article.getId(), 2);
		}
		if (ac.isSessionReady())
			ac.endSession();
	}
	
	public entryResponse update(String contribution, String critism, int expertise, int position, Article article, User reviewer, int review_id) {
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(review_id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				System.out.println("Cont:"+contribution);
				System.out.println("critism:"+critism);
				System.out.println("expertise:"+expertise);
				review.setContribution(contribution);
				review.setCritism(critism);
				review.setStatus(0);
				review.setReviewer(reviewer);
				review.setExpertise(expertise);
				review.setArticle(article);
				review.setPosition(position);
				session.update(review);
				session.getTransaction().commit();
			return entryResponse.SUCCESS;
			} else {
				return entryResponse.NOT_EXIST;
			}
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
			Criteria cr = session.createCriteria(Review.class);
			cr.add(Restrictions.eq("id", id));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				review = (Review)results.get(0);
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
	
	public entryResponse approveReview(int review_id) {
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(review_id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				review.setStatus(1);
				session.update(review);
				session.getTransaction().commit();
				
				Criteria cr = session.createCriteria(Review.class);
				cr.add(Restrictions.eq("reviewer", review.getReviewer()));
				List<Review> reviews = cr.list();
				int goodReviewCount = 0;
				for(int i = 0; i < reviews.size(); i++){
					if(reviews.get(i).getStatus() == 1){
						goodReviewCount++;
					}
				}	
				if(goodReviewCount >= 3){
					RoleController rc = new RoleController();
					rc.startSession();
					Role role = rc.get("activeauthor");
					if(rc.isSessionReady())
						rc.endSession();
									
					UserController uc = new UserController();
					uc.startSession();				
					uc.changeRole(review.getReviewer(), role);
					if(uc.isSessionReady()){
						uc.endSession();
					}
				}
			return entryResponse.SUCCESS;
			} else {
				return entryResponse.NOT_EXIST;
			}
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
	
	
	public entryResponse disapproveReview(int review_id) {
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(review_id).equals(entryResponse.EXIST)) {
				session = sessionFactory.openSession();				
				session.beginTransaction();
				review.setStatus(0);
				session.update(review);
				session.getTransaction().commit();
			return entryResponse.SUCCESS;
			} else {
				return entryResponse.NOT_EXIST;
			}
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
	Review review;
	
	

}