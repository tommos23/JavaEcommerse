

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserController uc = new UserController();
		String email = request.getParameter("uemail");
		String fname = request.getParameter("ufname");
		String lname = request.getParameter("ulname");
		request.setAttribute("fname", fname);
		request.setAttribute("lname", lname);
		request.setAttribute("email", email);
		boolean result = false;
		String alertMessage;
		String alertType ="info";
		try{
			result = uc.add_user(email, request.getParameter("upsw"),fname,lname);		
		}catch (Exception e){
			alertMessage = "<Strong>Oops!!</strong> Something went wrong.";
			alertType = "danger";
			request.setAttribute("alertMessage",alertMessage);
			request.setAttribute("alertType",alertType );
			RequestDispatcher dispatcher = request.getRequestDispatcher("signup");
			dispatcher.forward(request, response);
		}
		if(result){
			request.setAttribute("user","true"); //temp attr
			
			alertMessage =  "<Strong>Congratulations!!</strong> You have successfully registred.";
			alertType =  "success";
			request.setAttribute("alertMessage",alertMessage);
			request.setAttribute("alertType",alertType );
			response.setHeader("Location", "home");
			RequestDispatcher dispatcher = request.getRequestDispatcher("home");
			dispatcher.forward(request, response);
		}
		else{
			alertMessage = "<Strong>Oops!!</strong> Something went wrong.";
			alertType = "danger";
			request.setAttribute("alertMessage",alertMessage);
			request.setAttribute("alertType",alertType );
			response.setHeader("Location", "signup");
			RequestDispatcher dispatcher = request.getRequestDispatcher("signup");
			dispatcher.forward(request, response);
		}
	}

}
