package communication;

import messages.*;
import application.App;

public class MessageHandler implements MessageVisitor{
	private Connection connection;
	private App app;
	
	public MessageHandler(App app){
		connection = Connection.getInstance();
		this.app = app;
	}

	@Override
	public void visit(Accept accept) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(Auth auth) {
		// on serialise le message en Json
		String msgToJson = "";
		// on envoie le message
		connection.sendMsg(Protocol.CMD_AUTH, msgToJson);
	}

	@Override
	public void visit(Begin begin) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(ChooseGame chooseGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Create create) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Dice dice) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(Disconnect disconnect) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(GamesList gamesList) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(Join join) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Mvt mvt) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(NewPlayer newPlayer) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(Quit quit) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Refresh refresh) {
		connection.sendMsg(Protocol.CMD_REFRESH);
	}

	@Override
	public void visit(Refuse refuse) {
		// TODO Auto-generated method stub, server use only
		
	}

	@Override
	public void visit(Register register) {
		// on serialise la reponse
		String msgToJson = "";
		// on l'envoie
		connection.sendMsg(Protocol.CMD_REGISTER, msgToJson);
	}

	@Override
	public void visit(Roll roll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SelectGame selectGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(SendResult sendResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(StartGame startGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Start start) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(WinnerGame winnerGame) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Winner winner) {
		// TODO Auto-generated method stub
		
	}
}
