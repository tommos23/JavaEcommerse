import java.sql.*;
import java.util.*;

public class VersionModel {
	private int id;
	private int articles_id;
	private String title;
	private String abs;
	private int subject_id;
	private Date created_at
	
	VersionModel() {
	
	}

	public boolean setVersion(int articles_id, String title, String abs, int subject_id) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"articles_id","title", "abs", "subject_id"},{Integer.toString(articles_id), title, abs, Integer.toString(subject_id)}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("versions", data);
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
	public boolean setVersion(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("users",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				this.id  = id;
				articles_id = rs.getInt("articles_id");
				title = rs.getString("title");
				abs = rs.getString("abstract");
				subject_id = rs.getInt("subject_id");
				created_at = rs.getDate("created_at");

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

	//Get Version ID
	public int getVersionID() {
		return id;
	}
	
	//Get Version Article ID
	public int getVersionArticleID() {
		return articles_id;
	}
	
	// Get Version Title
	public String getVersionTitle(){
		return title;
	}
	
	//Get Vertsion Abstract
	public String getVersionAbstract(){
		return abs;
	}
	
	//Get Version Subject ID
	public int getVersionSubjectID(){
		return subject_id;
	}
	
	//Get User ID
	public Date getVersionCreatedAt(){
		return created_at;
	}
	
	//Get reviews for article
	public ArrayList<SubjectModel> getVersionSubjects() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`version_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("version_subjects",null,conditions);
			ArrayList<SubjectModel> subjects = new ArrayList<SubjectModel>();
			while(rs.next()){
				SubjectModel sm = new SubjectModel();
				sm.setKeyword(rs.getInt("subject_id"));
				reviews.add(sm);
			}
			return reviews;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}

}