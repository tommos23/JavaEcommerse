import java.sql.*;

public class DatabaseController {
	//Establish Database connection
	public Connection setConnection() {  
		try {  
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);

			// Open a connection
			conn = DriverManager.getConnection(DB_URL,USER,PASS);  
		} catch (Exception e) {  


		}  
		return conn;  

	}
	//End database connection
	public void closeConnection(){	
		try{
			if(stmt!=null)
				stmt.close();
		}catch(SQLException se2){
		}// nothing to do
		
		try{
			if(conn!=null)
				conn.close();
		}catch(SQLException se){
			se.printStackTrace();
		}
	}

	//Execute select query and get result set
	public ResultSet getResult(String table,String[] columns,String[] conditions ) {
		String sql ="SELECT ";
		if (columns == null)
			sql+="* FROM ";
		else{
			for(int i=0;i<columns.length;i++){
				if(i>0&&i<columns.length)
					sql +=",";
				sql += "`"+columns[i]+"`";				
			}
			sql+=" FROM ";
		}
		sql += "`"+table+"`";
		if (conditions != null){
			sql+=" WHERE ";
			for(int i=0;i<conditions.length;i++){
				if(i>0&&i<conditions.length)
					sql +=",";
				sql += conditions[i];				
			}
					
		}		
		//System.out.println(sql);
		ResultSet rst;
		
		try {
			// Execute SQL query
			stmt = conn.createStatement(); 
			rst = stmt.executeQuery(sql);
			return rst; 
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return null;
	  
	}  
	
	//Execute insert query
	public int insertData(String table,String[][] columnData ) {
		String sql ="INSERT INTO ";
		sql += "`"+table+"`";
		sql += "(";
		for(int i=0;i<columnData[0].length;i++){
			if(i>0&&i<columnData[0].length)
				sql +=",";
			sql += "`"+columnData[0][i]+"`";				
		}
		sql += ")";
		sql += " VALUES ";
		sql += "(";
		for(int i=0;i<columnData[0].length;i++){
			if(i>0&&i<columnData[0].length)
				sql +=",";
			sql += "?";
		}
		sql += ")";
		
		System.out.println(sql);
	
		try {
			//Prepare statement to avoid problem like SQL injection.
			PreparedStatement insertSql = conn.prepareStatement(sql);
			for(int i=1;i<=columnData[1].length;i++)
				insertSql.setString(i, columnData[1][i-1]);
			// Execute SQL query 
			int res = insertSql.executeUpdate();
			return res; 
		}
		catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
		}
		catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return 0;
	  
	} 

	// JDBC driver name and database URL
	private final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
	private final String DB_URL="jdbc:mysql://stusql.dcs.shef.ac.uk:3306/team130";

	//  Database credentials
	private final String USER = "team130";
	private final String PASS = "60dd53ae";

	//DB Access Variables
	Connection conn;
	Statement stmt;
}
