package messages;

/**
 * Indique la deconnexion d'un joueur de la partie
 */
public class Disconnect {
	private String leaverName;
	
	public Disconnect(){
		leaverName = "";
	}
	
	public Disconnect(String name){
		leaverName = name;
	}

	public String getLeaverName() {
		return leaverName;
	}

	public void setLeaverName(String leaverName) {
		this.leaverName = leaverName;
	}
}