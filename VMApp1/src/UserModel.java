import java.sql.*;

public class UserModel {
	private int id;
	private String email;
	private String password;
	private String fname;
	private String lname;

	public boolean setUser(String email,String password,String fname,String lname) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[][] data = {{"email","password", "fname", "lname"},{email,password,fname,lname}};

		// Extract data from result set
		try {
			int res = dbcon.insertData("users", data);
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
	public boolean setUser(String email){		
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		String[] conditions = new String[]{"`email` = \""+email+"\""};

		// Extract data from result set
		try {
			ResultSet rs = dbcon.getResult("users",null,conditions);
			if(rs.next()){
				//Retrieve by column name
				id  = rs.getInt("id");
				this.email = email;
				password = rs.getString("password");
				fname = rs.getString("fname");
				lname = rs.getString("lname");

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

	//Get User Password
	public String getUserPassword() {
		return password;
	}
	// Get User First Name
	public String getUserFirstName(){
		return fname;
	}
	//Get User Lastname
	public String getUserlastName(){
		return lname;
	}
	//Get User Email
	public String getUserEmail(){
		return email;
	}
	//Get User ID
	public int getUserId(){
		return id;
	}


}
