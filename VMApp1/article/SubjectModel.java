import java.sql.*;

public class SubjectModel {
	private int id;
	private String title;

	public boolean setSubject(String title) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"title"},{title}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("subjects", data);
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
	public boolean setSubject(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("subjects",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				this.id  = id;
				title = rs.getString("title");

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

	//Get Article ID
	public int getArticleID() {
		return password;
	}
	
	//Get Article Title
	public String getArticleTitle() {
		return title;
	}



}