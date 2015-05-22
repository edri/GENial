package communication;

import java.io.IOException;

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
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(auth);

			// on envoie le message
			connection.sendMsg(Protocol.CMD_AUTH, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Begin begin) {
		// TODO Auto-generated method stub, server use only

	}

	@Override
	public void visit(ChooseGame chooseGame) {
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(chooseGame);

			// on envoie le message
			connection.sendMsg(Protocol.CMD_CHOOSE_GAME, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Create create) {
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(create);

			// on envoie le message
			connection.sendMsg(Protocol.CMD_CREATE, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
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
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(join);

			// on envoie le message
			connection.sendMsg(Protocol.CMD_JOIN, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
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

		// on serialise le message en Json
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(register);
			System.out.println(msgToJson);

			// on envoie le message
			connection.sendMsg(Protocol.CMD_REGISTER, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(Roll roll) {
		connection.sendMsg(Protocol.CMD_ROLL);
	}

	@Override
	public void visit(SelectGame selectGame) {
		// TODO Auto-generated method stub, server use only

	}

	@Override
	public void visit(SendResult sendResult) {
		// on serialise le message en Json
		String msgToJson;
		try {
			msgToJson = JsonObjectMapper.toJson(sendResult);
			System.out.println(msgToJson);

			// on envoie le message
			connection.sendMsg(Protocol.SEND_RESULT, msgToJson);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void visit(StartGame startGame) {
		// TODO Auto-generated method stub, server use only

	}

	@Override
	public void visit(Start start) {
		connection.sendMsg(Protocol.CMD_START);
	}

	@Override
	public void visit(WinnerGame winnerGame) {
		// TODO Auto-generated method stub, server use only

	}

	@Override
	public void visit(Winner winner) {
		// TODO Auto-generated method stub, server use only

	}
}
