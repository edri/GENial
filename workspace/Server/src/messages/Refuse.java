package messages;

import communication.MessageHandler;

/**
 * Indique le refus de la demande recue
 */
public class Refuse extends Message {
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
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
}