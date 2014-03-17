public class UserController {
	public boolean validate_user(String uemail,String pwd) {
		umodel = new UserModel();
		if(umodel.setUser(uemail)){
			return pwd.equals(umodel.getUserPassword());
		}
		else 
			return false;
	}
	
	public boolean add_user(String uemail,String upsw,String ufname,String ulname) throws Exception{
		umodel = new UserModel();
		return umodel.setUser(uemail, upsw, ufname, ulname);		
	}
	
	UserModel umodel;
}
