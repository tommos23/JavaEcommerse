
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.tools.view.VelocityViewServlet;

public class GetWelcome extends VelocityViewServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Template handleRequest( HttpServletRequest request, 
			HttpServletResponse response, Context context )
	{
		HttpSession session = request.getSession(true);		
		session.setMaxInactiveInterval(30*60);
		//------Code to display alert message------
		if(session.getAttribute("alertMessage")!=null){
			context.put("alertMessage",session.getAttribute("alertMessage").toString());
			context.put("showAlert", "true");
			if(session.getAttribute("alertType") != null)
				context.put("alertType",session.getAttribute("alertType").toString());
			else
				context.put("alertType", "info");
			session.setAttribute("alertMessage", null);
			session.setAttribute("alertType", null);
		}
		//-----End of Alert Message Code---------
		/* get the template */
		Template template = null;
		if (!session.isNew()){
			if(session.getAttribute("user") != null){
				if(session.getAttribute("user").equals("true")){
					try {
						response.sendRedirect("home");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		else {
			context.put("application", "Login Application");
			//if(session.getAttribute("user") != null)
			//	context.put("user",session.getAttribute("user").toString());
			try {
				if(request.getAttribute("fname")!=null)
					context.put("fname", request.getAttribute("fname").toString());
				if(request.getAttribute("lname")!=null)
					context.put("lname", request.getAttribute("lname").toString());
				if(request.getAttribute("email")!=null)
					context.put("email", request.getAttribute("email").toString());
			} catch(Exception e ) {
				System.out.println("Error " + e);
			}

		}
		try {
			template = getTemplate("index.vm"); 
		} catch(Exception e ) {
			System.out.println("Error " + e);
		}
		return template;
	}
}
