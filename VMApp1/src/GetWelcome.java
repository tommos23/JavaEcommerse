
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
    	  if(request.getAttribute("fname")!=null)
    		  context.put("fname", request.getAttribute("fname").toString());
    	  if(request.getAttribute("lname")!=null)
    		  context.put("lname", request.getAttribute("lname").toString());
    	  if(request.getAttribute("email")!=null)
    		  context.put("email", request.getAttribute("email").toString());
         template = getTemplate("user/signup.vm"); 
      } catch(Exception e ) {
         System.out.println("Error " + e);
      }
      try {
         template = getTemplate("index.vm"); 
      } catch(Exception e ) {
         System.out.println("Error " + e);
      }
      return template;
   }
}
