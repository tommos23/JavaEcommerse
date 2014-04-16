import java.sql.*;
import java.util.Date;

public class CommentModel {
	private int id;
	private int review_id;
	private int user_id;
	private String text;
	private Date created_at;

	public boolean setComment(int review_id, int user_id, String text) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"review_id","user_id", "text"},{Integer.toString(review_id),Integer.toString(user_id),text}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("comments", data);
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
	public boolean setComment(int id){	
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = "+id};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("comment",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				this.id = id;
				review_id = rs.getInt("review_id");
				user_id = rs.getInt("user_id");
				text = rs.getString("text");
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

	//Get Comment ID
	public int getCommentId(){
		return id;
	}
	// Get comment review id
	public int getReviewId() {
		return review_id;
	}
	// Get comment user id
	public int getUserId(){
		return user_id;
	}
	//Get Submission Guidelines
	public String getText(){
		return text;
	}
	//Get Created At
	public Date getCreatedAt(){
		return created_at;
	}
	
	// Object retrieval methods
	// Get user model for comment
	public UserModel getUserModel() {
		UserModel user = new UserModel();
		user.setUser(String.valueOf(user_id));
		return user;
	}
	// Get review model for comment
	public ReviewModel getReviewModel() {
		ReviewModel review = new ReviewModel();
		review.setReview(review_id);
		return review;
	}
	

}