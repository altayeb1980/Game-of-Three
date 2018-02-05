package com.takeaway.game;

import java.util.UUID;

import com.takeaway.game.model.Game;
import com.takeaway.game.model.Game.GameStatus;
import com.takeaway.game.model.Player;
import com.takeaway.game.model.PlayerInput;

public class BaseGameTest {

	protected String gameId;

	
	public BaseGameTest() {
		gameId = UUID.randomUUID().toString();
	}

	public Game createGame(String player1Name, String player2Name) {
		Game game = new Game(gameId);
		if (player1Name != null) {
			Player player1 = new Player(player1Name);
			game.setGameStatus(GameStatus.JOIN);
			game.setPlayer1(player1);
		}
		if (player2Name != null) {
			Player player2 = new Player(player2Name);
			game.setGameStatus(GameStatus.JOIN);
			game.setPlayer2(player2);
		}
		return game;
	}

	public PlayerInput createPlayerInput(String playerName, String gameId) {
		PlayerInput playerInput = new PlayerInput();
		playerInput.setGameId(gameId);
		playerInput.setPlayerName(playerName);
		return playerInput;
	}
}
