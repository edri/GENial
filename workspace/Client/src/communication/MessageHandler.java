package communication;

public class MessageHandler {
	private Connection connection;
	
	public MessageHandler(){
		connection = Connection.getInstance();
	}
	
	public void processPing(){
		connection.sendMsg("ping", "");
	}
	
	public void processPong(){
		connection.sendMsg("pong", "");
	}

}
