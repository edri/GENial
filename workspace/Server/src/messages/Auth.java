package messages;

import communication.MessageHandler;

/**
 * authentification d'un utilisateur
 */
public class Auth extends Message {
	private String name;
	private String hashPwd;
	
	public Auth() {
		// TODO Auto-generated constructor stub
		name = "";
		hashPwd = "";
	}
	public Auth(String n, String pwd){
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
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}