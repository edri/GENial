-----------------------------------------------------
-- Client -> Serveur
-----------------------------------------------------

-- OK
-- AUTH (String pseudo, String hashPassword) : Json =>  authentification d’un utilisateur.
-- Authentification
SELECT * 
FROM player
WHERE (player.username = 'username' AND player.password = 'password_sha-1');

-- OK 
-- REGISTER (String pseudo, String hashPassword) : Json => inscription d’un utilisateur.
INSERT INTO player (username, password) 
VALUES ('username', 'password');

-- REFRESH () : JSon => demande de la liste des parties disponibles. 
-- Liste de parties, avec le nom de la partie, le nb de joueurs actuel, le 
-- nb de joueurs max 
SELECT game.creator, COUNT(*) as nb_players
FROM game
	INNER JOIN player_game ON game.id = player_game.game
GROUP BY player_game.game;	


-- Créer une partie
-- CREATE (String nom, int nbJoueurs, int difficulte, int nbCases) : Json => créer une partie.
INSERT INTO game (creator, difficulty, squares)
VALUES ('creator', 'difficulty', 'squares');


--	SEND_RESULT (int score) : JSon => envoi du résultat du mini-jeu.
-- Envoi du résultat du mini-jeu
INSERT INTO result (player, turn, score, dice_score)
VALUES ('player', 'turn', 'score', 'dice_score');



-- JOIN (String nomPartie) : JSon => rejoindre une partie.
-- QUIT () : JSon => arrêt de la partie (utilisable par l’administrateur uniquement).
-- START () : JSon => commencer la partie en attente.
-- ROLL () : JSon => faire une demande de lancer de dé.
-- CHOOSE_GAME (int idGame) : JSon => sélection du mini-jeu.



-----------------------------------------------------
-- Serveur -> Client
-----------------------------------------------------


--	WINNER_GAME (String nomJoueur, int scote) : JSon => indique le gagnant du mini-jeu.
SELECT player AS winner
FROM result 
HAVING result.score >= ALL (SELECT result.score 
							FROM result);
							
							
-- Liste des parties disponibles
SELECT game.id AS name,  
FROM game
WHERE 1;
							

							
							
-- BEGIN (int difficulty) : JSon => indique aux clients que le jeu peut commencer.
-- DICE (String nomJoueur) : JSon => indique qui doit lancer le dé (envoyé à tout le monde).
-- MVT (int nbSquare) : JSon => indique le nombre de case dont le joueur doit avancer.
-- SELECT_GAME (Map<int, String> games) : JSon  => indique qu’un client doit choisir un mini-jeu.
-- START_GAME (int code, int seed) : JSon => indique que le mini-jeu peut commencer.
-- WINNER (String nomJoueur) : JSon => indique le nom du joueur qui a gagné la partie (les clients stoppent ensuite la partie).
-- DISCONNECT (String nomJoueur) : JSon => indique qu’un joueur a perdu la connexion.
-- Lorsque rien n’est précisé, le serveur renvoie des chaînes de caractères sous format Json, contenant :
-- Le statut : ACCEPT ou REFUSE.
-- Un message d’erreur identifié par ERROR, qui peut être vide.









