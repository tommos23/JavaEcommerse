import java.sql.*;
import java.util.*;

public class KeywordModel {
	private int id;
	private String keyword;

	KeywordModel() {
	
	}
	
	public boolean setKeyword(String keyword) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"keyword"},{keyword}};
		try {
			int res = dbcon.insertData("keywords",data);
			if(res == 1) {
				return true;
			} else {
				return false;
			}
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setKeyword(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("keywords",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				this.id  = id;
				keyword = rs.getString("keyword");

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

	//Get Keyword
	public String getKeyword() {
		return password;
	}

	//Get Keyowrd ID
	public int getKeyowrdId(){
		return id;
	}
	
	//Get keywords for article
	public ArrayList<ArticleModel> getKeywordArticles() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`keyword_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("article_keywords",null,conditions);
			ArrayList<ArticleModel> articles = new ArrayList<ArticleModel>();
			while(rs.next()){
				ArticleModel am = new ArticleModel();
				am.setKeyword(rs.getInt("article_id"));
				articles.add(am);
			}
			return articles;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	
	
	//Get users for keyword
	public ArrayList<UserModel> getKeywordArticles() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`keyword_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("keyword_interest",null,conditions);
			ArrayList<UserModel> users = new ArrayList<UserModel>();
			while(rs.next()){
				UserModel um = new UserModel();
				um.setUser(rs.getInt("user_id"));
				users.add(um);
			}
			return users;
		} catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	
}
