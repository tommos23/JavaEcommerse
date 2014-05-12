package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ArticleController;
import com.jg.Controller.UserController;
import com.jg.Model.Article;
import com.jg.Model.User;
import com.jg.Services.EmailService;

/**
 * Servlet implementation class UserValidate
 */
public class ApproveUpload extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ApproveUpload() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		uc.startSession();
		User thisUser = null;
		if (session.getAttribute("user_id") != null) {
			thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
		}
		if (thisUser != null && (thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher"))) {
			int id = 0;
			if (request.getParameter("id") != null) 
				id = Integer.parseInt(request.getParameter("id"));
			ArticleController ac = new ArticleController();
			ac.startSession();
			switch(ac.approveUpload(id)){
			case SUCCESS:
				session.setAttribute("alertMessage","Article is successfully approved.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("ApproveArticles");
				Article a = ac.get(id);
				ac.endSession();
				EmailService es = new EmailService();
				try {
					//send email to author
					String email =  a.getMainAuthor().getEmail();
					String sub = "Your article is approved";
					String title = a.getLatestVersion().getTitle();
					String msg = "<html><body>Dear "+a.getMainAuthor().getFirstname()+",<br><br> This is to inform that your article is approved by the editor."+
							"<br><b>Article Details :</b><table><tr><td>Article Name :</td><td>"+title+"</td></tr></table>"+
							" Make sure that you complete 3 peer reviews to process it futher for publishing, Thank You.<br><br>Regards,<br>Team JAMR</body></html>";
					es.sendEmail(email,sub,msg);
					// Send email to main contact
					String conname = a.getContactName();
					String email1 =  a.getContactEmail();
					String sub1 = "Your article is approved";
					String msg1 = "<html><body>Dear "+conname+",<br><br> This is to inform you that article you have been assigned to is approved by the editor."+
							"<br><b>Article Details :</b><table><tr><td>Article Name :</td><td>"+title+"</td></tr></table>"+
							"If you are not the above mentioned person  or have any problems with this article then please <a href=\"mailto:test@test.com\">Email Us</a>"+
							"<br><br>Regards,<br>Team JAMR</body></html>";
					es.sendEmail(email1,sub1,msg1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case FAIL:
				session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Please check article number.");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ApproveArticles");
				break;
			case DB_ERROR:
				session.setAttribute("user","false");
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("ApproveArticles");
				break;
			}
			if (uc.isSessionReady())
				uc.endSession();
			if(ac.isSessionReady())
				ac.endSession();
		}
		else {
			if (uc.isSessionReady())
				uc.endSession();
			String alertMessage = "<Strong>Oops!!</strong> You do not have permission to do that.";
			String alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
