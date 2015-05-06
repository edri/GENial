package communication;

public class MessageExtractor {
	private MessageHandler msgHandler;
	
	public MessageExtractor(){
		msgHandler = new MessageHandler();
	}
	
	public void process(String ident, String msgJson){
		switch(ident){
		case "ping":
			System.out.println("Ping");
			msgHandler.processPong();
			break;
		default:
			//error
			break;
		}
	}

}
