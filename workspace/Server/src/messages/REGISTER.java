package messages;

/**
 * Demande de creation d'un nouveau compte
 */
public class REGISTER {
	private String name;
	private String pwd;
	
	public REGISTER() {
		name = "";
		pwd = "";
	}
	public REGISTER(String name, String pwd){
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
