

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
		response.sendRedirect("welcome");
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		UserModel um = new UserModel();
		um.setUser(request.getParameter("uemail"));
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		boolean redirectHome = false;
		if(session.getAttribute("user") != null)
			if(session.getAttribute("user").equals("true"))
				redirectHome = true;
		if(redirectHome)
			response.sendRedirect("home");
		else{
			boolean result = false;
			if(um.getUserPassword() != null)
				if(um.getUserPassword().equals(request.getParameter("upwd")))
					result = true;				
			if(result){
				session.removeAttribute("falseAttempt");
				session.setAttribute("user_id", um.getUserID());
				session.setAttribute("user","true");
				session.setAttribute("fname",um.getUserFirstname());
				session.setAttribute("lname",um.getUserSurname());
				session.setAttribute("alertMessage","<Strong>Welcome!!</strong> You have successfully logged in.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("home");
			}
			else{
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
			}
		}

	}

}
