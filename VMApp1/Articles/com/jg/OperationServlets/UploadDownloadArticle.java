package com.jg.OperationServlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import com.jg.Services.EmailService;

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
		boolean user = false;
		if(session.getAttribute("user") != null){
			if(session.getAttribute("user").equals("true"))
				user = true;
		}
		else{
			session.setAttribute("user", "false");
		}

		if (!user){
			try {
				response.sendRedirect("welcome");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{

			String title = "";
			String conname = "";
			String conemail = "";
			String abs = "";
			String keywords = "";
			String filepath = "";
			Set<Integer> subIds = new HashSet<Integer>(0);
			Set<String> newSubs = new HashSet<String>(0);
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
						System.out.println(attrName);
						if(attrName.equals("title"))
							title = fileItem.getString();
						else if (attrName.equals("abstract"))
							abs = fileItem.getString();
						else if (attrName.equals("contactname"))
							conname = fileItem.getString();
						else if (attrName.equals("contactemail"))
							conemail = fileItem.getString();
						else if (attrName.equals("keywords"))
							keywords = fileItem.getString();
						else if (attrName.equals("subjects[]"))
							subIds.add(Integer.parseInt(fileItem.getString()));
						else if(attrName.equals("newsubs[]"))
							newSubs.add(fileItem.getString());
					}
				}

				ac.startSession();
				switch(ac.addNewArticle(title,conname,conemail, abs, keywords, subIds, newSubs, filepath , session.getAttribute("user_email").toString())){
				case SUCCESS:					
					session.setAttribute("alertMessage","Article is successfully uploaded.");
					session.setAttribute("alertType","success" );
					response.sendRedirect("home");
					EmailService es = new EmailService();
					try {
						//send email to author
						String email =  session.getAttribute("user_email").toString();
						String sub = "Successfully uploaded article.";
						String msg = "<html><body>Dear "+session.getAttribute("user_fname").toString()+",<br><br> This is to confirm that you have successfully uploaded Article."+
						" Complete 3 peer reviews to process it futher for publishing, Thank You.<br><br>Regards,<br>Team JAMR</body></html>";
						es.sendEmail(email,sub,msg);
						// Send email to main contact
						String email1 =  conemail;
						String sub1 = "Successfully uploaded article.";
						String msg1 = "<html><body>Dear "+conname+",<br><br> This is to confirm that you have been assigned as a main contact for article by "+session.getAttribute("user_fname").toString()+
										"<br><b>Article Details :</b><table><tr><td>Article Name :</td><td>"+title+"</td></tr><tr><td>Abstract:</td><td>"+abs+"</td></tr></table>"+
										"If you are not the person mentioned above or have any problems with this article then please <a href=\"mailto:test@test.com\">Email Us</a>"+
										"<br><br>Regards,<br>Team JAMR</body></html>";
						es.sendEmail(email1,sub1,msg1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
}
