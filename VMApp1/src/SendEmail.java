import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jg.Services.EmailService;

/**
 * Servlet implementation class UserValidate
 */
public class SendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendEmail() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EmailService es = new EmailService();
		try {
			String email = request.getParameter("email");
			String sub = request.getParameter("sub");
			String msg = request.getParameter("msg");
			es.sendEmail(email,sub,msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}