package com.jg.Controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jg.Model.*;
import com.jg.Model.Version;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

public class UserController extends Controller{
	public validateResponse validate(String email,String password){
		try{
			if(isExist(email).equals(entryResponse.EXIST)){
				if(user.getPassword().equals(password)){
					if(!isSessionReady()) throw new Exception();
					session = sessionFactory.openSession();				
					session.beginTransaction();
					user.setOldLastlogin(user.getNewLastlogin());
					user.setNewLastlogin(new Date());
					session.update(user);
					session.getTransaction().commit();
					return validateResponse.VALID;
				}
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
		finally{
			if(session.isOpen())
				session.close();
			System.out.println("session closed.");
		}
	}

	public entryResponse addNew(String firstname,String surname,String email,String password){
		if(isExist(email).equals(entryResponse.EXIST))
			return entryResponse.EXIST;
		else{
			try{
				if(!isSessionReady()) throw new Exception();
				session = sessionFactory.openSession();				
				session.beginTransaction();
				
				user= new User(firstname,surname,email,password,"na",new Date(),new Date(),new Date());
				Criteria cr = session.createCriteria(Role.class);
				cr.add(Restrictions.eq("name", "reader"));
				List result = cr.list();
				user.setRole((Role)result.get(0));
				
				session.save(user);
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
	}

	private entryResponse isExist(String email){
		try{				
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.eq("email", email));
			List results = cr.list();				
			session.getTransaction().commit();			
			if(!results.isEmpty()){
				user = (User)results.get(0);
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

	public User getUser() {
		return user;
	}

	public static enum validateResponse {VALID,INVALID,DB_ERROR}
	public static enum entryResponse {EXIST,NOT_EXIST,SUCCESS,DB_ERROR}
	private User user;
	Session session = null;
}
