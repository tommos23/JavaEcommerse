

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
		// TODO Auto-generated constructor stub
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("welcome");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		UserModel um = new UserModel();
		String email = request.getParameter("uemail");
		String fname = request.getParameter("ufname");
		String lname = request.getParameter("ulname");		
		boolean result = false;
		int res=0;
		String alertMessage;
		String alertType ="info";
		try{
			if(um.setUser(email))
				res = 2;
			else {
				session.setAttribute("fname", fname);
				session.setAttribute("lname", lname);
				session.setAttribute("email", email);
				result = um.setUser(email, request.getParameter("upsw"),fname,lname);
				if(result)
					res = 1;
				else
					res = 999;
			}
		}catch (Exception e){
			res = 999;
		}
		
		switch(res){
		case 1:
			session.setAttribute("user_id", um.getUserID());
			session.setAttribute("user","true");
			session.setAttribute("fname",um.getUserFirstname());
			session.setAttribute("lname",um.getUserSurname());			
			alertMessage =  "<Strong>Congratulations!!</strong> You have successfully registred.";
			alertType =  "success";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("home");
			break;
		case 2:
			session.setAttribute("user","false");
			session.setAttribute("existemail",email);
			alertMessage = "<Strong>This eamil is already registerd with us. Try using sign-in.";
			alertType = "warning";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("welcome");
			break;
		default:
			session.setAttribute("user","false");
			alertMessage = "<Strong>Oops!!</strong> Something went wrong.";
			alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			response.sendRedirect("welcome");
			break;
		}
	}

}
