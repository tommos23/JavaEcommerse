package com.jg.OperationServlets;
import com.jg.Controller.*;


import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserRegister
 */
public class UserRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserRegister() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("welcome");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);

		String fname = request.getParameter("ufname");
		String lname = request.getParameter("ulname");
		String email = request.getParameter("uemail");
		String pwd = request.getParameter("upwd");
		String alertMessage;
		String alertType ="info";
		UserController uc = new UserController();
		uc.startSession();
		switch(uc.addNew(fname,lname,email,pwd)){
		case SUCCESS:
			session.setAttribute("user","true");
			//session.setAttribute("user_id", uc.getUser().getId());
			session.setAttribute("user_fname",uc.getUser().getFirstname());
			session.setAttribute("user_lname",uc.getUser().getSurname());
			session.setAttribute("user_email",uc.getUser().getEmail());
			session.setAttribute("last_login", "na");
			alertMessage =  "<Strong>Congratulations!!</strong> You have successfully registred.";
			alertType =  "success";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("home");
			break;
		case EXIST:
			session.setAttribute("user","false");
			session.setAttribute("existemail",email);
			alertMessage = "<Strong>This eamil is already registerd with us. Try using sign-in.";
			alertType = "warning";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("welcome");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			alertMessage = "<Strong>Oops!!</strong> Something went wrong.";
			alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("welcome");
			break;
		}
		if(uc.isSessionReady())
			uc.endSession();
	}

}
