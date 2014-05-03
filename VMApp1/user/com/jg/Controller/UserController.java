package com.jg.Controller;

import java.util.Date;
import java.util.List;

import com.jg.Model.*;

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
				
				user= new User(firstname,surname,email,password,"n/a",new Date(),new Date(),new Date());
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

	public entryResponse isExist(String email){
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
	
	public User get(int id) {
		List<User> user = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = sessionFactory.openSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			cr.add(Restrictions.eq("id", id));
			user = cr.list();
			session.getTransaction().commit();
			if (user.size() > 0) {
				return user.get(0);
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

	private User user;
	Session session = null;
}
