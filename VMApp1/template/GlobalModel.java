import java.sql.*;

public class GlobalModel {
	private int id;
	private String name;
	private String goals;
	private String submission_guidelines;

	public boolean setGlobal(String name, String goals, String submission_guidelines) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"name","goals", "submission_guidelines"},{name,goals,submission_guidelines}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("global", data);
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
	public boolean setGlobal(int id){	
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("global",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				this.id = id;
				name = rs.getString("name");
				goals = rs.getString("goals");
				submission_guidelines = rs.getString("submission_guidelines");

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

	//Get Global ID
	public int getGlobalId(){
		return id;
	}
	// Get Global Name
	public String getGlobalName() {
		return name;
	}
	// Get Global Goals
	public String getGlobalGoals(){
		return goals;
	}
	//Get Submission Guidelines
	public String getSubmissionGuidelines(){
		return submission_guidelines;
	}


}