package messages;

/**
 * Demande de creation d'un nouveau compte
 */
public class Register {
	private String name;
	private String pwd;
	
	public Register(){
		name = "";
		pwd = "";
	}
	
	public Register(String name, String pwd){
		this.name = name;
		this.pwd = pwd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}