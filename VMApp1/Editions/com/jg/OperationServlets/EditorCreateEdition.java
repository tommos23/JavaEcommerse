package com.jg.OperationServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jg.Controller.ArticleController;
import com.jg.Controller.EditionController;
import com.jg.Controller.LettersToEditorsController;
import com.jg.Controller.UserController;
import com.jg.Controller.VolumeController;
import com.jg.Model.Edition;
import com.jg.Model.User;
import com.jg.Model.Volume;

/**
 * Servlet implementation class UserValidate
 */

public class EditorCreateEdition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditorCreateEdition() {
		super();
	}

	@SuppressWarnings("incomplete-switch")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
		UserController uc = new UserController();
		User thisUser = null;
		try{
			uc.startSession();
			if (session.getAttribute("user_id") != null) {
				thisUser = uc.get(Integer.parseInt(session.getAttribute("user_id").toString()));
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(uc.isSessionReady())
				uc.endSession();
		}

		VolumeController vc = new VolumeController();
		EditionController ec = new EditionController();
		ArticleController ac = new ArticleController();
		LettersToEditorsController ltec = new LettersToEditorsController();
		if (thisUser != null){
			if(thisUser.getRole().getName().equals("editor") || thisUser.getRole().getName().equals("publisher")) {
				try{
					int volume_id = 0;
					if(request.getParameter("volume_id") != null){
						volume_id = Integer.parseInt(request.getParameter("volume_id"));
					} else {
						session.setAttribute("alertMessage","No volume ID");
						session.setAttribute("alertType","danger" );
						response.sendRedirect("EditorViewVolume");
					}
					String description = request.getParameter("description");
					if (description == null || description.equals("")){
						session.setAttribute("alertMessage","Please write a description.");
						session.setAttribute("alertType","danger" );
						response.sendRedirect("EditorNewEdition");
					}
					vc.startSession();
					Volume vol = vc.get(volume_id);
					if(vc.isSessionReady())
						vc.endSession();


					ec.startSession();			
					if (vol == null){
						response.sendRedirect("EditorNewEdition");
					}

					switch(ec.create(description, vol)){
					case SUCCESS:
						session.setAttribute("alertMessage","Edition Created.");
						session.setAttribute("alertType","success" );
						response.sendRedirect("EditorViewEditions?id="+volume_id);
						break;
					case FAIL:
						session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Volume not created.");
						session.setAttribute("alertType","danger" );
						response.sendRedirect("EditorViewVolumes");
						break;
					case DB_ERROR:
						session.setAttribute("user","false");
						session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
						session.setAttribute("alertType","danger");
						response.sendRedirect("EditorViewVolumes");
						break;
					}
					Edition edition = ec.get(description, vol);
					if(ec.isSessionReady())
						ec.endSession();

					String[] articles = request.getParameterValues("articles");
					ac.startSession();
					if(articles != null && articles.length != 0){
						for (int i = 0; i < articles.length; i++) {
							ac.publishArticle(Integer.parseInt(articles[i]), edition);
						}
					}
					if(ac.isSessionReady())
						ac.endSession();

					String[] letters = request.getParameterValues("letters");
					ltec.startSession();
					if(articles != null && articles.length != 0){
						for (int i = 0; i < articles.length; i++) {
							ltec.publishLetter(Integer.parseInt(letters[i]), edition);
						}
					}
					if(ltec.isSessionReady())
						ltec.endSession();
				}
				catch(Exception e){
					e.printStackTrace();
				}
				finally{
					if(vc.isSessionReady())
						vc.endSession();
					if(ec.isSessionReady())
						ec.endSession();
					if(ac.isSessionReady())
						ac.endSession();
					if(ltec.isSessionReady())
						ltec.endSession();
				}
			}
			else {
				String alertMessage = "<Strong>Oops!!</strong> You do not have permission to do that.";
				String alertType = "danger";
				session.setAttribute("alertMessage",alertMessage);
				session.setAttribute("alertType",alertType );
				try {
					response.sendRedirect("welcome");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			String alertMessage = "<Strong>Oops!!</strong> You do not have permission to do that.";
			String alertType = "danger";
			session.setAttribute("alertMessage",alertMessage);
			session.setAttribute("alertType",alertType );
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
