package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.UserController;

/**
 * Servlet implementation class UserValidate
 */
public class UserValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserValidate() {
		super();
		// TODO Auto-generated constructor stub
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		boolean redirectHome = false;
		if(session.getAttribute("user") != null)
			if(session.getAttribute("user").equals("true"))
				redirectHome = true;
		if(redirectHome)
			response.sendRedirect("home");
		else{
			response.sendRedirect("welcome");
		}
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);		

		String email = request.getParameter("uemail");
		String pwd = request.getParameter("upwd");
		UserController uc = new UserController();
		uc.startSession();
		switch(uc.validate(email, pwd)){
		case VALID:
			session.removeAttribute("falseAttempt");
			session.setAttribute("user","true");
			session.setAttribute("user_id",uc.getUser().getId());
			session.setAttribute("user_fname",uc.getUser().getFirstname());
			session.setAttribute("user_lname",uc.getUser().getSurname());
			session.setAttribute("user_email",uc.getUser().getEmail());
			session.setAttribute("last_login", uc.getUser().getOldLastlogin().toLocaleString());
			session.setAttribute("alertMessage","<Strong>Welcome!!</strong> You have successfully logged in.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("home");
			break;
		case INVALID:
			session.setAttribute("user","false"); //temp attr
			session.setAttribute("existemail",request.getParameter("uemail"));
			if (session.isNew())
				session.setAttribute("falseAttempt",1);			
			else{
				if(session.getAttribute("falseAttempt") != null){
					int count = (Integer)session.getAttribute("falseAttempt");
					count++;
					session.setAttribute("falseAttempt",count);	
				}
				else
					session.setAttribute("falseAttempt",1);	
			}
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check your username & password. Failed login Attempt: "+session.getAttribute("falseAttempt"));
			session.setAttribute("alertType","danger" );
			response.sendRedirect("welcome");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("welcome");
			break;
		}
		if(uc.isSessionReady())
			uc.endSession();
	}

}
