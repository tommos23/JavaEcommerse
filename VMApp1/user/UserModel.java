import java.sql.*;

public class UserModel {
	private int id;
	private int role_id;
	private String email;
	private String password;
	private String firstname;
	private String surname;
	private Date created_at;
	private Timestamp last_login;

	public boolean setUser(String email,String password,String fname,String lname) {
		DatabaseController dbcon = new DatabaseController();
		dbcon.setConnection();
		Object[][] data = {{"email","password", "fname", "lname"},{email,password,fname,lname}};

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
				firstname = rs.getString("fname");
				surname = rs.getString("lname");
				role_id = rs.getInt("role_id");
				created_at =  rs.getDate("created_at");
				last_login =  rs.getTimestamp("last_login");
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
	public String getUserFirstname(){
		return firstname;
	}
	//Get User Surname
	public String getUserSurname(){
		return surname;
	}
	//Get User Email
	public String getUserEmail(){
		return email;
	}
	//Get User ID
	public int getUserID(){
		return id;
	}
	//Get user role id
	public int getUserRoleID(){
		return role_id;
	}
	public Timestamp getLastLogin(){
		return last_login;
	}
	public Date getCreatedDate(){
		return created_at;
	}


}
