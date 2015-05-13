package protocol;

public abstract class Protocol {
    // Commandes Client => Serveur
	public final static String CMD_AUTH = "AUTH";
	public final static String CMD_REGISTER = "REGISTER";
	public final static String CMD_REFRESH = "REFRESH";
	public final static String CMD_CREATE = "CREATE";
	public final static String CMD_JOIN = "JOIN";
	public final static String CMD_QUIT = "QUIT";
	public final static String CMD_START = "START";
	public final static String CMD_ROLL = "ROLL";
	public final static String CMD_CHOOSE_GAME = "CHOOSE_GAME";
	public final static String SEND_RESULT = "SEND_RESULT";
    
    // Commandes Serveur => Client
	public final static String CMD_NEW_PLAYER = "NEW_PLAYER";
	public final static String CMD_BEGIN = "BEGIN";
	public final static String CMD_DICE = "DICE";
	public final static String CMD_MVT = "CMD_MVT";
	public final static String CMD_SELECT_GAME = "SELECT_GAME";
	public final static String CMD_START_GAME = "START_GAME";
	public final static String CMD_WINNER_GAME = "WINNER_GAME";
	public final static String CMD_WINNER = "WINNER";
	public final static String CMD_DISCONNECT = "DISCONNECT";
	
	public final static String CMD_ACCEPT = "ACCEPT";
	public final static String CMD_REFUSE = "REFUSE";
    
}
