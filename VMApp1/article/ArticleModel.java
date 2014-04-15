import java.sql.*;
import java.util.*;

public class ArticleModel {
	private int id;
	private int main_author_id;
	private int status;
	private int volume;
	private int edition;
	private Date created_at;
	

	public boolean setArticle(int main_author_id, int status, int volume, int edition) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"main_author_id","status", "volume", "edition"},{Integer.toString(main_author_id), Integer.toString(status), Integer.toString(volume), Integer.toString(edition)}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("articles", data);
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
	public boolean setArticle(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("articles",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				this.id = id;
				main_author_id  = rs.getInt("main_author_id");
				status  = rs.getInt("status");
				volume  = rs.getInt("volume");
				edition  = rs.getInt("edition");
				created_at  = rs.getDate("created_at");

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
		return id;
	}
	// Get main author id
	public int getArticleMainAuthorID(){
		return main_author_id;
	}
	//Get User Lastname
	public int getArticleStatus(){
		return status;
	}
	//Get User Email
	public int getArticleVolume(){
		return volume;
	}
	//Get User ID
	public int getArticleEdition(){
		return edition;
	}
	
	public Date getArticleCreatedAt(){
		return created_at;
	}
	
	//Get keywords for article
	public ArrayList<KeywordModel> getArticleKeywords() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`article_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("article_keywords",null,conditions);
			ArrayList<KeywordsModel> keywords = new ArrayList<KeywordsModel>();
			while(rs.next()){
				KeywordModel kwm = new KeywordModel();
				kwm.setKeyword(rs.getInt("keyword_id"));
				keywords.add(kwm);
			}
			return keywords;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	
	//Get versions for article
	public ArrayList<VersionModel> getArticleVersions() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`article_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("versions",null,conditions);
			ArrayList<VersionModel> versions = new ArrayList<VersionModel>();
			while(rs.next()){
				VersionModel vm = new VersionModel();
				vm.setVersion(rs.getInt("id"));
				versions.add(vm);
			}
			return versions;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	
	//Get reviews for article
	public ArrayList<ReviewModel> getArticleVersions() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`article_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("reviews",null,conditions);
			ArrayList<ReviewModel> reviews = new ArrayList<ReviewModel>();
			while(rs.next()){
				ReviewModel rm = new ReviewModel();
				rm.setKeyword(rs.getInt("id"));
				reviews.add(rm);
			}
			return reviews;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}

}