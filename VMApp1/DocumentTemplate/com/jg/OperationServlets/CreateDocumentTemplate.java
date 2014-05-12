package com.jg.OperationServlets;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.jg.Controller.DocumentTemplateController;

/**
 * Servlet implementation class UserValidate
 */
public class CreateDocumentTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateDocumentTemplate() {
		super();
	}
	
	private ServletFileUpload uploader = null;
	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		String name = null;
		String description = null;
		String format = null;
		String url = null;
		
		try {
			String filepath = "";
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

					File file = new File("/share/student/stucat016/context/uploads"+File.separator+fileItem.getName());
					System.out.println("Absolute Path at server="+file.getAbsolutePath());
					fileItem.write(file);
					filepath = file.getCanonicalPath();
					url = file.getAbsolutePath();
					System.out.println("File "+fileItem.getName()+ " uploaded successfully.");
					System.out.println("<br>");
					System.out.println("<a href=\"UploadDownloadFileServlet?fileName="+fileItem.getName()+"\">Download "+fileItem.getName()+"</a>");
				} else {
					String attrName = fileItem.getFieldName();
					if (attrName.equals("name"))
						name = fileItem.getString();
					else if (attrName.equals("description"))
						description = fileItem.getString();
					else if (attrName.equals("format"))
						format = fileItem.getString();
				}
					
			}
		}catch(Exception e){
			System.out.println(e.getLocalizedMessage());
		}
		
		if (name == null || name.equals("")){
			session.setAttribute("alertMessage","Please write a name.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		
		if (description == null || description.equals("")){
			session.setAttribute("alertMessage","Please write a description.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		
		if (format == null || format.equals("")){
			session.setAttribute("alertMessage","Please write a format.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		
		System.out.println("URL" + url);
		if (url == null || url.equals("")){
			session.setAttribute("alertMessage","Please write a url.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		
		DocumentTemplateController dc = new DocumentTemplateController();
		dc.startSession();
		switch(dc.create(name,description,format,url)){
		case SUCCESS:
			session.setAttribute("alertMessage","Volume Created.");
			session.setAttribute("alertType","success" );
			response.sendRedirect("ViewDocumentTemplate");
			break;
		case FAIL:
			session.setAttribute("alertMessage","<Strong>Sorry!!</strong> Volume not created.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			break;
		case DB_ERROR:
			session.setAttribute("user","false");
			session.setAttribute("alertMessage","<Strong>Oops!!</strong> Something went wrong. Try Again");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			break;
		}
		if(dc.isSessionReady())
			dc.endSession();
	}
}

