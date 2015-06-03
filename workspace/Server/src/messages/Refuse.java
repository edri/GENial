package messages;

/**
 * Indique le refus de la demande recue
 */
public class Refuse {
	private String reason;
	
	public Refuse() {
		reason = "";
	}
	
	public Refuse(String reason){
		this.reason = reason;
	}
	
	public String getReason(){
		return reason;
	}
	
	public void setReason(String str){
		reason = str;
	}
}