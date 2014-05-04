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
import com.jg.Controller.VolumeController;
import com.jg.Model.Edition;
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60);
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
		VolumeController vc = new VolumeController();
		vc.startSession();
		System.out.print("VOLUME ID ");
		System.out.println(volume_id);
		Volume vol = vc.get(volume_id);
		if(vc.isSessionReady())
			vc.endSession();
		
		EditionController ec = new EditionController();
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
		ArticleController ac = new ArticleController();
		ac.startSession();
		if(articles != null && articles.length != 0){
			for (int i = 0; i < articles.length; i++) {
				ac.publishArticle(Integer.parseInt(articles[i]), edition);
			}
		}
		if(ac.isSessionReady())
			ac.endSession();
		
		String[] letters = request.getParameterValues("letters");
		LettersToEditorsController ltec = new LettersToEditorsController();
		ltec.startSession();
		if(articles != null && articles.length != 0){
			for (int i = 0; i < articles.length; i++) {
				ltec.publishLetter(Integer.parseInt(letters[i]), edition);
			}
		}
		if(ltec.isSessionReady())
			ltec.endSession();
	}
}
