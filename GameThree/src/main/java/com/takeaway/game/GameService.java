package com.takeaway.game;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.takeaway.game.Game.GameStatus;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@Service
public class GameService {
	@Autowired
	private GameRepository gameRepository;

	private SecureRandom rand = new SecureRandom();
	private int min = 1;
	private int max = 100;
	private AtomicLong resultAtomicNumber = new AtomicLong();
	private String operation = "0";

	@PostConstruct
	public void init() {
		rand.setSeed(new Date().getTime());
	}

	public Game play(PlayerInput playerInput) {
		long currentNumber = resultAtomicNumber.get();
		long nextNumber = getNextNumber(currentNumber);
		long resultNumber = getResultNumber(nextNumber);

		Game game = getGameById(playerInput.getGameId());
		game.setContent("");
		Player player = getPlayer(game, playerInput.getPlayerName());
		player.setOldNumber(nextNumber);
		player.setResultNumber(resultNumber);
		game.setCurrentPlayerName(player.getName());

		if (resultNumber == 1) {
			game.setGameStatus(GameStatus.WIN);
			game.setContent(player.getName() + " WIN!!!. (" + operation + " to " + currentNumber + " = " + nextNumber
					+ ")" + " with result number: " + resultNumber);
			return game;
		}
		resultAtomicNumber.set(resultNumber);
		game.setGameStatus(GameStatus.PLAY);
		game.setContent(player.getName() + " add (" + operation + " to " + currentNumber + " = " + nextNumber + ")"
				+ " with result number: " + resultNumber);
		return game;
	}

	private long getResultNumber(long nextNumber) {
		long resultNumber = nextNumber / 3;
		return resultNumber;
	}

	private long getNextNumber(long currentNumber) {
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
		Player player = getPlayer(game, playerInput.getPlayerName());
		int inceptRandomeNumber = rand.nextInt((max - min) + 1) + min;
		resultAtomicNumber.set(inceptRandomeNumber);
		player.setOldNumber(inceptRandomeNumber);
		game.setPlayer1(player);
		game.setGameStatus(GameStatus.START);
		return game;
	}

	public void setResultAtomicNumber(AtomicLong resultAtomicNumber) {
		this.resultAtomicNumber = resultAtomicNumber;
	}
	
	
}
