package communication;

import messages.*;

interface MessageVisitor 
{
	void visit(Accept accept);
	void visit(Auth auth);
	void visit(Begin begin);
	void visit(ChooseGame chooseGame);
	void visit(Create create);
	void visit(Dice dice);
	void visit(Disconnect disconnect);
	void visit(GamesList gamesList);
	void visit(Join join);
	void visit(Mvt mvt);
	void visit(NewPlayer newPlayer);
	void visit(Quit quit);
	void visit(Refresh refresh);
	void visit(Refuse refuse);
	void visit(Register register);
	void visit(Roll roll);
	void visit(SelectGame selectGame);
	void visit(SendResult sendResult);
	void visit(StartGame startGame);
	void visit(Start start);
	void visit(WinnerGame winnerGame);
	void visit(Winner winner);
}
