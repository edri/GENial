package messages;

/**
 * Indique qu'une erreur s'est produite et fournit le message lie a l'erreur
 */
public class ERROR {
	private String errorMsg;
	
	public ERROR(String msg){
		errorMsg = msg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
