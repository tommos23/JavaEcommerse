package com.jg.OperationServlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.jg.Controller.ArticleController;

/**
 * Servlet implementation class UploadDownloadArticle
 */
@WebServlet
public class UploadDownloadArticle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletFileUpload uploader = null;
	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}

	public UploadDownloadArticle() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);

		String title = null;
		String abs = null;
		String keywords = null;
		String filepath = null;
		ArticleController ac = new ArticleController();	
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		};
		try {
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			Iterator<FileItem> fit = fileItemsList.iterator();
			//System.out.println("size"+fileItemsList.size());
			while(fit.hasNext()){
				FileItem fileItem = fit.next();
				if(!fileItem.isFormField()){
					System.out.println("FieldName="+fileItem.getFieldName());
					System.out.println("FileName="+fileItem.getName());
					System.out.println("ContentType="+fileItem.getContentType());
					System.out.println("Size in bytes="+fileItem.getSize());

					File file = new File("uploads"/*this.getServletContext().getAttribute("FILES_DIR")*/+File.separator+fileItem.getName());
					System.out.println("Absolute Path at server="+file.getAbsolutePath());
					fileItem.write(file);
					filepath = file.getCanonicalPath();
					System.out.println("File "+fileItem.getName()+ " uploaded successfully.");
					System.out.println("<br>");
					System.out.println("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");

				}
				else{
					String attrName = fileItem.getFieldName();
					//System.out.println(attrName);
					if(attrName.equals("title"))
						title = fileItem.getString();
					else if (attrName.equals("abstract"))
						abs = fileItem.getString();
					else if (attrName.equals("keywords"))
						keywords = fileItem.getString();
				}
			}
			
			ac.startSession();
			switch(ac.addNewArticle(title, abs, keywords, filepath , session.getAttribute("user_email").toString())){
			case SUCCESS:					
				session.setAttribute("alertMessage","Article is successfully uploaded.");
				session.setAttribute("alertType","success" );
				response.sendRedirect("home");
				break;
			default:
				session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
				session.setAttribute("alertType","danger" );
				response.sendRedirect("home");
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("home");
		}
		if(ac.isSessionReady())
			ac.endSession();
	}


}
