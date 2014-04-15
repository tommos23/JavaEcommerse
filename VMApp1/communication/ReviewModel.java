import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

public class ReviewModel {
	private int id;
	private int reviewer_id;
	private int article_id;
	private int position;
	private Date created_at;
	private String contribution;
	private String critism;
	private int expertise;
	private int status;

	public boolean setReview(int reviewer_id, int article_id, int position, String contribution, String critism, int expertise, int status) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"reviewer_id","article_id", "position", "contribution","critism", "expertise", "status"},{Integer.toString(reviewer_id),Integer.toString(article_id),Integer.toString(position),contribution,critism,Integer.toString(expertise),Integer.toString(status)}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("reviews", data);
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
	public boolean setReview(int id){	
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("reviews",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				this.id = id;
				reviewer_id = rs.getInt("reviewer_id");
				article_id = rs.getInt("article_id");
				position = rs.getInt("position");
				created_at = rs.getDate("created_at");
				contribution = rs.getString("contribution");
				critism = rs.getString("critism");
				expertise = rs.getInt("expertise");
				status = rs.getInt("status");

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

	// Get id
	public int getReviewId() {
		return id;
	}
	// Get reviewer id
	public int getReviewerId() {
		return reviewer_id;
	}
	// Get article id
	public int getArticleId(){
		return article_id;
	}
	//Get position
	public int getPosition(){
		return position;
	}
	//Get Created At
	public Date getCreatedAt(){
		return created_at;
	}
	//Get contribution
	public String getContribution() {
		return contribution;
	}
	//Get critism
	public String getCritism() {
		return critism;
	}
	//Get expertise
	public int getExpertise(){
		return expertise;
	}
	//Get status
	public int getStatus(){
		return expertise;
	}

	// Object retrieval methods
	// Get user model for review
	public UserModel getUserModel() {
		UserModel user = new UserModel();
		user.setUser(reviewer_id);
		return user;
	}
	// Get article for review
	public ArticleModel getArticleModel() {
		ArticleModel article = new ArticleModel();
		article.setArticle(article_id);
		return article;
	}
	// Get comments for review
	public ArrayList<CommentModel> getCommentModels() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`review_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("comments",null,conditions);
			ArrayList<CommentModel> comments = new ArrayList<CommentModel>();
			while(rs.next()){
				CommentModel comment = new CommentModel();
				comment.setComment(rs.getInt("id"));
				comments.add(comment);
			}
			return comments;
		}
		catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	// Get messages to the editors for review
	public ArrayList<MessageToEditorsModel> getMessageToTheEditorsModels() {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`review_id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("message_to_editors",null,conditions);
			ArrayList<MessageToEditorsModel> messages = new ArrayList<MessageToEditorsModel>();
			while(rs.next()){
				MessageToEditorsModel message = new MessageToEditorsModel();
				message.setComment(rs.getInt("id"));
				messages.add(message);
			}
			return messages;
		}
		catch (SQLException e) {
			//Handles errors with resultset
			e.printStackTrace();
			return null;
		}
	}
	
}