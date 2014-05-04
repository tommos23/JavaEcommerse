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
	
	private boolean isMultipart;
	private String filePath;
	private int maxFileSize = 1000 * 1024;
	private int maxMemSize = 200 * 1024;
	private File file;
	
	public void init(){
	      // Get the file location where it would be stored.
	      filePath = getServletContext().getInitParameter("file-upload"); 
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		session.setMaxInactiveInterval(30*60); 
		
		// Check that we have a file upload request
	    isMultipart = ServletFileUpload.isMultipartContent(request);
		
	    if(!isMultipart){
	    	session.setAttribute("alertMessage","No file Uploaded");
	    	session.setAttribute("alertType","danger" );
	    	response.sendRedirect("NewDocumentTemplates");
	    	return;
	    }
	    DiskFileItemFactory factory = new DiskFileItemFactory();
		// maximum size that will be stored in memory
		factory.setSizeThreshold(maxMemSize);
		// Location to save data that is larger than maxMemSize.
		factory.setRepository(new File("temp"));
		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		// maximum file size to be uploaded.
		upload.setSizeMax( maxFileSize );
		 
		try{ 
			// Parse the request to get file items.
			List fileItems = upload.parseRequest(request);
				
			// Process the uploaded file items
			Iterator i = fileItems.iterator();
		    
			 while (i.hasNext()) 
		      {
		         FileItem fi = (FileItem)i.next();
		         if (!fi.isFormField ())	
		         {
		            // Get the uploaded file parameters
		            String fieldName = fi.getFieldName();
		            String fileName = fi.getName();
		            String contentType = fi.getContentType();
		            boolean isInMemory = fi.isInMemory();
		            long sizeInBytes = fi.getSize();
		            // Write the file
		            if( fileName.lastIndexOf("\\") >= 0 ){
		               file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
		            }else{
		               file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
		            }
		            fi.write( file ) ;
		         }
		      }
			
			
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
		
		
		String name = request.getParameter("name");
		if (name == null || name.equals("")){
			session.setAttribute("alertMessage","Please write a name.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		String description = request.getParameter("description");
		if (description == null || description.equals("")){
			session.setAttribute("alertMessage","Please write a description.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		String format = request.getParameter("format");
		if (format == null || format.equals("")){
			session.setAttribute("alertMessage","Please write a format.");
			session.setAttribute("alertType","danger" );
			response.sendRedirect("NewDocumentTemplate");
			return;
		}
		String url = request.getParameter("url");
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

