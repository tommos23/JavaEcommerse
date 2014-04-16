import java.sql.*;
import java.util.Date;

public class MessageToEditorsModel {
	private int id;
	private int user_id;
	private int review_id;
	private String comment;
	private Date created_at;

	public boolean setMessageToEditors(int user_id, int review_id, String comment) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"user_id","review_id","comment"},{Integer.toString(user_id),Integer.toString(review_id),comment}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("message_to_editors", data);
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
	
	public boolean setMessageToEditors(int id){	
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = \""+id+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("message_to_editors",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				this.id = id;
				review_id = rs.getInt("review_id");
				user_id = rs.getInt("user_id");
				comment = rs.getString("comment");
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

	// Get id
	public int getMessageToEditorsId() {
		return id;
	}
	// Get review id
	public int getReviewId() {
		return review_id;
	}
	// Get user id
	public int getUserId(){
		return user_id;
	}
	//Get position
	public String getComment(){
		return comment;
	}
	//Get Created At
	public Date getCreatedAt(){
		return created_at;
	}
	
	// Object retrieval methods
	// Get user model for message
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