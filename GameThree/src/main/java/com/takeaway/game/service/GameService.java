package com.takeaway.game.service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takeaway.game.exceptions.GameNotFoundException;
import com.takeaway.game.exceptions.GameOperationException;
import com.takeaway.game.model.Game;
import com.takeaway.game.model.Player;
import com.takeaway.game.model.PlayerInput;
import com.takeaway.game.model.Game.GameStatus;
import com.takeaway.game.repository.GameRepository;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@Service
public class GameService {
	private static final int GAME_END_NUMBER = 1;

	@Autowired
	private GameRepository gameRepository;

	private final SecureRandom rand = new SecureRandom();
	private final int min = 1;
	private final int max = 100;
	private AtomicLong resultAtomicNumber = new AtomicLong();
	

	@PostConstruct
	public void init() {
		rand.setSeed(new Date().getTime());
	}

	public Game play(PlayerInput playerInput) {
		Game game = getGameById(playerInput.getGameId());
		validateGame(playerInput, game);
		long currentNumber = resultAtomicNumber.get();
		OperationResult operationResult = new OperationResult(currentNumber);
		long nextNumber = operationResult.getNextNumber();
		long resultNumber = operationResult.getResultNumber();
		String operation = operationResult.getOperation();
		Player player = getPlayer(game, playerInput.getPlayerName());
		player.setOldNumber(nextNumber);
		player.setResultNumber(resultNumber);
		game.setCurrentPlayerName(player.getName());
		if (resultNumber == GAME_END_NUMBER) {
			return endGame(game, currentNumber, nextNumber, resultNumber, operation, player);
		}
		return continueGame(game, currentNumber, nextNumber, resultNumber, operation, player);
	}

	private Game continueGame(Game game, long currentNumber, long nextNumber, long resultNumber, String operation,
			Player player) {
		resultAtomicNumber.set(resultNumber);
		game.setGameStatus(GameStatus.PLAY);
		game.setContent(player.getName() + " add (" + operation + " to " + currentNumber + " = " + nextNumber + ")"
				+ " with result number: " + resultNumber);
		return game;
	}

	private Game endGame(Game game, long currentNumber, long nextNumber, long resultNumber, String operation,
			Player player) {
		game.setGameStatus(GameStatus.FINISH);
		game.setContent(player.getName() + " WIN!!!. (" + operation + " to " + currentNumber + " = " + nextNumber
				+ ")" + " with result number: " + resultNumber);
		return game;
	}

	private void validateGame(PlayerInput playerInput, Game game) {
		if(game == null) {
			throw new GameNotFoundException("Invalid gameId");
		}
	
		if(playerInput == null || playerInput.getPlayerName() == null) {
			throw new GameOperationException("player can't be null");
		}
		
		if(playerInput.getPlayerName().equals(game.getCurrentPlayerName())) {
			throw new GameOperationException("player can't play twice");
		}
	}

	private Game createNewGame(String player1Name) {
		Game game = new Game(UUID.randomUUID().toString());
		Player player = new Player(player1Name);
		game.setGameStatus(GameStatus.JOIN);
		game.setPlayer1(player);
		gameRepository.addOrUpdateGame(game);
		return game;
	}

	public Game jointGame(String playerName) {
		Game game = getFirstOpenGame();
		if (game == null) {
			return createNewGame(playerName);
		}
		Player player = new Player(playerName);
		game.setPlayer2(player);
		game.setGameStatus(GameStatus.JOIN);
		gameRepository.addOrUpdateGame(game);
		return game;
	}

	public Game getFirstOpenGame() {
		return gameRepository.getFirstOpenGame();
	}

	public Set<Game> lisGames() {
		return gameRepository.lisGames();
	}

	public Game getGameById(String gameId) {
		return gameRepository.getGameById(gameId);
	}

	public Player getPlayer(Game game, String playerName) {
		Player player = null;
		if (game.getPlayer1().getName().equalsIgnoreCase(playerName)) {
			player = game.getPlayer1();
		} else {
			player = game.getPlayer2();
		}
		return player;
	}

	public Game startGame(PlayerInput playerInput) {
		Game game = getGameById(playerInput.getGameId());
		if (game == null)
			throw new GameNotFoundException("invalid gameId");
		Player player = getPlayer(game, playerInput.getPlayerName());
		int inceptRandomeNumber = generateRandomNumber();
		resultAtomicNumber.set(inceptRandomeNumber);
		player.setOldNumber(inceptRandomeNumber);
		game.setPlayer1(player);
		game.setGameStatus(GameStatus.START);
		return game;
	}

	private int generateRandomNumber() {
		return rand.nextInt((max - min) + 1) + min;
	}

	class OperationResult {
		private String operation;
		private final long currentNumber;

		OperationResult(final long currentNumber) {
			this.currentNumber = currentNumber;
		}

		private long getResultNumber() {
			return getNextNumber() / 3;
		}

		private long getNextNumber() {
			if (currentNumber % 3 == 0) {
				operation = "0";
				return currentNumber;
			} else if ((currentNumber + 1) % 3 == 0) {
				operation = "+1";
				return (currentNumber + 1);
			} else {
				operation = "-1";
				return (currentNumber - 1);
			}
		}

		public String getOperation() {
			return operation;
		}

	}

}
