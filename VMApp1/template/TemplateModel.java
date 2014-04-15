import java.sql.ResultSet;
import java.sql.SQLException;


public class TemplateModel {
	private String name;
	private String description;
	private String format;
	private String url;
	private int id;

	public boolean setTemplate(String name,String description,String format,String url){
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"name","description", "format", "url"},{name,description,format,url}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("templates", data);
			if(res==1)
				return true;
			else
				return false;
		} catch (Exception e) {
			//Handles errors with resultset
			e.printStackTrace();
			return false;
		}
	}
	public boolean setTemplate(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = "+id+""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("templates",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				name = rs.getString("name");
				description = rs.getString("description");
				format = rs.getString("format");
				url = rs.getString("url");

				// Clean-up Resources
				rs.close();
				dbcon.closeConnection();
				return true;
			}
			else 
				return false; 
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return false;
		}
	}	

	public String getName(){
		return name;
	}
	public String getDescription(){
		return description;
	}
	public String getForamt(){
		return format;
	}
	public String getURL(){
		return url;
	}
	public int getId(){
		return id;
	}
}
