package messages;

/**
 * authentification d'un utilisateur
 */
public class AUTH {
	private String name;
	private String hashPwd;
	
	public AUTH(String n, String pwd){
		name = n;
		hashPwd = pwd;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPwd(){
		return hashPwd;
	}
	
	public void setName(String n){
		name = n;
	}
	
	public void setPwd(String pwd){
		hashPwd = pwd;
	}
}
