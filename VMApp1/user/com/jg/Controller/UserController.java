package com.jg.Controller;

import java.util.Date;
import java.util.List;

import com.jg.Model.*;

import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("deprecation")
public class UserController {



	public validateResponse validate(String email,String password){
		try{
			if(isExist(email).equals(entryResponse.EXIST)){
				if(user.getPassword().equals(password))
					return validateResponse.VALID;
				else
					return validateResponse.INVALID;
			}
			else{
				return validateResponse.INVALID;
			}
		}
		catch(Exception e){
			e.printStackTrace();
			return validateResponse.DB_ERROR;
		}		
	}

	public entryResponse addNew(String firstname,String surname,String email,String password){
		if(isExist(email).equals(entryResponse.EXIST))
			return entryResponse.EXIST;
		else{
			SessionFactory sf = null;
			Session session = null;
			try{				
				user= new User(firstname,surname,email,password,"na",new Date(),new Date());
				sf = new AnnotationConfiguration().configure().buildSessionFactory();
				session = sf.openSession();				
				session.beginTransaction();				
				session.save(user);
				session.getTransaction().commit();

				return entryResponse.SUCCESS;
			}
			catch(Exception e){
				e.printStackTrace();
				return entryResponse.DB_ERROR;
			}
			finally{
				session.close();
				sf.close();
			}
		}
	}

	private entryResponse isExist(String email){
		SessionFactory sf = null;
		Session session = null;
		try{				
			sf = new AnnotationConfiguration().configure().buildSessionFactory();
			session = sf.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.eq("email", email));
			List results = cr.list();				
			session.getTransaction().commit();
			session.close();
			sf.close();
			if(!results.isEmpty()){
				user = (User)results.get(0);
				System.out.println(((User)results.get(0)).getEmail()+" size "+results.size());
				return entryResponse.EXIST;
			}
			else
				return entryResponse.NOT_EXIST;
		}
		catch(Exception e){
			e.printStackTrace();
			session.close();
			sf.close();
			return entryResponse.DB_ERROR;
		}
	}

	public User getUser() {
		return user;
	}

	public static enum validateResponse {VALID,INVALID,DB_ERROR}
	public static enum entryResponse {EXIST,NOT_EXIST,SUCCESS,DB_ERROR}
	private User user;
}
