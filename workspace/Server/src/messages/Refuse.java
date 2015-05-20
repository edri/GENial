package messages;

import communication.MessageHandler;

/**
 * Indique le refus de la demande recue
 */
public class Refuse extends Message {
	private String reason;
	
	public Refuse(String reason) { 
		this.reason = reason;
	}
	
	@Override
	public void accept(MessageHandler visitor) {
		visitor.visit(this);
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason = reason;
	}
}
