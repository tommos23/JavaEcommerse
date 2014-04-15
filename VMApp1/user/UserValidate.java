

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		UserController uc = new UserController();
		
	    boolean result = uc.validate_user(request.getParameter("uemail"), request.getParameter("upsw"));		
		if(result){
			/*
			 * Attention Needed: set attributes for first and last name to be displayed on home screen
			 */
			request.setAttribute("user","true"); //temp attr
			request.setAttribute("alertMessage","<Strong>Welcome!!</strong> You have successfully logged in.");
			request.setAttribute("alertType","success" );
			response.setHeader("Location", "home");
			RequestDispatcher dispatcher = request.getRequestDispatcher("home");
			dispatcher.forward(request, response);
		}
		else{
			request.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check your username & password.");
			request.setAttribute("alertType","danger" );
			response.setHeader("Location", "welcome");
			RequestDispatcher dispatcher = request.getRequestDispatcher("welcome");
			dispatcher.forward(request, response);
		}
		
	}

}
