import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
      /* get the template */
      Template template = null;
      context.put("application", "Login Application");
      if(request.getAttribute("user") != null)
      	context.put("user",request.getAttribute("user").toString());
      
      //------Code to display alert message------
      if(request.getAttribute("alertMessage")!=null){
    	  context.put("alertMessage",request.getAttribute("alertMessage").toString());
    	  context.put("showAlert", "true");
    	  if(request.getAttribute("alertType") != null)
    		  context.put("alertType",request.getAttribute("alertType").toString());
    	  else
    		  context.put("alertType", "info");
      }
      //-----End of Alert Message Code---------
      try {
         template = getTemplate("user/login.vm"); 
      } catch(Exception e ) {
         System.out.println("Error " + e);
      }
      return template;
   }
}
