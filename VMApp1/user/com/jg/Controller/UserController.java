package com.jg.Controller;

import java.util.Date;

import com.jg.Model.*;

import org.hibernate.*;
import org.hibernate.cfg.AnnotationConfiguration;

@SuppressWarnings("deprecation")
public class UserController {
	


	public validateResponse validate(String email,String password){
		try{
			if(user.getPassword().equals(password))
				return validateResponse.VALID;
			else
				return validateResponse.INVALID;
		}
		catch(Exception e){
			e.printStackTrace();
			return validateResponse.DB_ERROR;
		}		
	}
	
	public registerResponse addNew(String firstname,String surname,String email,String password){
		if(isExist(email))
			return registerResponse.EXIST;
		else{
			try{
				user= new User();
				user.setFirstname(firstname);
				user.setSurname(surname);
				user.setEmail(email);
				user.setPassword(password);
				user.setCreatedat(new Date());
				user.setLastlogin(new Date());
				SessionFactory sf = new AnnotationConfiguration().configure().buildSessionFactory();
				Session session = sf.openSession();
				session.beginTransaction();
				session.save(user);
				session.getTransaction().commit();
				session.close();
				sf.close();
				return registerResponse.SUCCESS;
			}
			catch(Exception e){
				e.printStackTrace();
				return registerResponse.DB_ERROR;
			}
		}
	}
	
	private boolean isExist(String email){
		return false;
	}
	
	public User getUser() {
		return user;
	}
	
	public static enum validateResponse {VALID,INVALID,DB_ERROR}
	public static enum registerResponse {EXIST,SUCCESS,DB_ERROR}
	private User user;
}
