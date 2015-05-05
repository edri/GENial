package protocol;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AuthMessage {
	private String pseudo;
	private String hashPassword;
	
	public String getPseudo() {
		return pseudo;
	}
	
	public String getHashPassword() {
		return hashPassword;
	}
	
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	
	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}
}
