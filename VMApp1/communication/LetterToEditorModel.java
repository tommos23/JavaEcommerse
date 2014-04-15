import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class LetterToEditorModel {
	private int id;
	private int user_id;
	private int article_id;
	private String text;
	private String edited_text;
	private String reply_text;
	private String status;
	private String publish_edition;
	private Date created_at;
	private Date edited_at;
	private Date replyed_at;


	public boolean setLetter(int user_id,int article_id,String text){
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		Object[][] data = {{"user_id","article_id", "text", "status"},{user_id,article_id,text,"new"}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("letters_to_the_editor", data);
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
	public boolean setLetter(int id){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`id` = "+id+""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("letters_to_the_editor",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id = rs.getInt("id");
				user_id = rs.getInt("user_id");
				article_id = rs.getInt("article_id");
				text = rs.getString("text");
				edited_text = rs.getString("edited_text");
				reply_text = rs.getString("reply_text");
				status = rs.getString("status");
				publish_edition = rs.getString("publish_edition");
				created_at = rs.getDate("created_at");
				edited_at = rs.getDate("edited_at");
				replyed_at = rs.getDate("replyed_at");

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

	public int getArticleId(){
		return article_id;
	}
	public int getUserId(){
		return user_id;
	}
	public String getText(){
		return text;
	}
	public String getEditedText(){
		return edited_text;
	}
	public String getReplyText(){
		return reply_text;
	}
	public String getStatus(){
		return status;
	}
	public String getPublishedEdition(){
		return publish_edition;
	}
	public Date getCreatedDate(){
		return created_at;
	}
	public Date getEditedDate(){
		return edited_at;
	}	
	public Date getReplyedDate(){
		return replyed_at;
	}
	public int getId(){
		return id;
	}
}
