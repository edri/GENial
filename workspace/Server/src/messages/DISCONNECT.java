package messages;

/**
 * Indique la deconnexion d'un joueur de la partie
 */
public class DISCONNECT {
	private String leaverName;
	
	public DISCONNECT(String name){
		leaverName = name;
	}

	public String getLeaverName() {
		return leaverName;
	}

	public void setLeaverName(String leaverName) {
		this.leaverName = leaverName;
	}
}
