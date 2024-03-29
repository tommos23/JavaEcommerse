package com.jg.Controller;

import java.util.Date;
import java.util.List;
import com.jg.Model.*;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserController extends Controller{
	public validateResponse validate(String email,String password){
		MessageDigest messageDigest;
		String saltedPassword = password+salt;
		String encryptedPassword = "";
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(saltedPassword.getBytes());			
			byte[] mdbytes = messageDigest.digest();
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        encryptedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		try{
			if(isExist(email).equals(entryResponse.EXIST)){
				if(user.getPassword().equals(encryptedPassword)){
					if(!isSessionReady()) throw new Exception();
					session = HibernateUtil.getSessionFactory().getCurrentSession();				
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
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}

	public entryResponse addNew(String firstname,String surname,String email,String password){
		if(isExist(email).equals(entryResponse.EXIST))
			return entryResponse.EXIST;
		else{
			try{
				if(!isSessionReady()) throw new Exception();
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				MessageDigest messageDigest;
				String saltedPassword = password+salt;
				String encryptedPassword = "";
				try {
					messageDigest = MessageDigest.getInstance("SHA-256");
					messageDigest.update(saltedPassword.getBytes());					
					byte[] mdbytes = messageDigest.digest();
			        StringBuffer sb = new StringBuffer();
			        for (int i = 0; i < mdbytes.length; i++) {
			          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
			        }
			        encryptedPassword = sb.toString();					
				} catch (NoSuchAlgorithmException e1) {
					e1.printStackTrace();
				}
				user= new User(firstname,surname,email,encryptedPassword,"n/a",new Date(),new Date(),new Date());
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
				if(session.isOpen()){
					session.close();
					System.out.println("session closed.");
				}
			}
		}
	}

	public entryResponse isExist(String email){
		try{				
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();
			if(!session.getTransaction().isActive())
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
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}

	public User getUser() {
		return user;
	}
	public User getUser(String email){
		isExist(email);
		return user;
	}
	
	public User get(int id) {
		List<User> user = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
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
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	
	public List<User> getUsers() {
		List<User> users = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			users = cr.list();
			session.getTransaction().commit();
			return users;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	
	public List<User> getEditors() {
		List<User> users = null;
		try{
			if(!isSessionReady()) throw new Exception();
			session = HibernateUtil.getSessionFactory().getCurrentSession();				
			session.beginTransaction();
			Criteria cr = session.createCriteria(User.class);
			users = cr.list();
			session.getTransaction().commit();
			for (int i = users.size(); i >= 0; i--) {
				System.out.println(users.get(i).getRole().getName());
				if (!users.get(i).getRole().getName().equals("editor") && !users.get(i).getRole().getName().equals("publisher")) {
					users.remove(i);
				}
			}
			return users;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
		finally{
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	
	public entryResponse changeRole(User user, Role role){
		try{
			if(!isSessionReady()) throw new Exception();
			if (isExist(user.getEmail()) != null) {
				session = HibernateUtil.getSessionFactory().getCurrentSession();				
				session.beginTransaction();
				user.setRole(role);
				session.update(user);
				session.getTransaction().commit();
				return entryResponse.SUCCESS;
			}
			else
				return entryResponse.NOT_EXIST;

		}
		catch(Exception e){
			e.printStackTrace();
			if (session.isOpen())
				session.close();
			return entryResponse.DB_ERROR;
		}
		finally{
			if(session.isOpen()){
				session.close();
				System.out.println("session closed.");
			}
		}
	}
	
	private String salt = "27a65236f0376c5f1866366a42d3effa";
	private User user;
	Session session = null;
}
