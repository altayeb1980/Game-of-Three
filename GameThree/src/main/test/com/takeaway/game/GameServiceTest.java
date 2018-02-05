package com.takeaway.game;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.takeaway.game.model.Game;
import com.takeaway.game.model.Player;
import com.takeaway.game.model.PlayerInput;
import com.takeaway.game.model.Game.GameStatus;
import com.takeaway.game.repository.GameRepository;
import com.takeaway.game.service.GameService;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

	@Mock
	private GameRepository gameRepository;

	@InjectMocks
	private GameService gameService;

	private String player1Name = "altayeb";
	private String player2Name = "emmy";

	@Test
	public void TestJoinGame_WhenNoPlayerExitYet() {
		Mockito.when(gameRepository.getFirstOpenGame()).thenReturn(null);
		Game game = createGame(player1Name, null);
		Game newGame = gameService.jointGame(player1Name);
		Mockito.verify(gameRepository).addOrUpdateGame(newGame);
		Assert.assertEquals(game.getPlayer1().getName(), newGame.getPlayer1().getName());
		Assert.assertTrue(newGame.getPlayer1().getOldNumber() == 0);
		Assert.assertEquals(newGame.getGameStatus().name(), GameStatus.JOIN.name());
	}

	@Test
	public void TestJoinGame_WhenPlayerOneExit() {
		Game game = createGame(null, player2Name);
		Mockito.when(gameRepository.getFirstOpenGame()).thenReturn(game);
		Game newGame = gameService.jointGame(player2Name);
		Mockito.verify(gameRepository).addOrUpdateGame(newGame);
		Assert.assertEquals(game.getPlayer2().getName(), newGame.getPlayer2().getName());
		Assert.assertTrue(newGame.getPlayer2().getOldNumber() == 0);
		Assert.assertEquals(newGame.getGameStatus().name(), GameStatus.JOIN.name());
	}

	@Test
	public void TestStartGame() {
		Game game = createGame(player1Name, null);
		Mockito.when(gameRepository.getGameById(Mockito.any(String.class))).thenReturn(game);

		PlayerInput playerInput = createPlayerInput(player1Name, game.getId());
		Game actualGame = gameService.startGame(playerInput);

		Assert.assertEquals(actualGame.getPlayer1().getName(), game.getPlayer1().getName());
		Assert.assertEquals(actualGame.getGameStatus().name(), GameStatus.START.name());
		Assert.assertTrue(actualGame.getPlayer1().getOldNumber() > 0);
	}

	@Test
	public void test_Player1Win() {
		Game game = createGame(player1Name, player2Name);
		Mockito.when(gameRepository.getGameById(Mockito.any(String.class))).thenReturn(game);

		PlayerInput playerInput = createPlayerInput(player1Name, game.getId());

		gameService.setResultAtomicNumber(new AtomicLong(4));

		Game actualGame = gameService.play(playerInput);
		Assert.assertNotNull(actualGame);

		Assert.assertEquals(actualGame.getGameStatus().name(), GameStatus.FINISH.name());
		Assert.assertEquals(actualGame.getPlayer1().getOldNumber(), 3);
		Assert.assertEquals(actualGame.getPlayer1().getResultNumber(), 1);

		String operation = "-1";
		Assert.assertEquals(
				player1Name + " WIN!!!. (" + operation + " to " + 4 + " = " + 3 + ")" + " with result number: " + 1,
				actualGame.getContent());

	}

	@Test
	public void test_Player2Win() {
		Game game = createGame(player1Name, player2Name);
		Mockito.when(gameRepository.getGameById(Mockito.any(String.class))).thenReturn(game);

		PlayerInput playerInput = createPlayerInput(player1Name, game.getId());

		gameService.setResultAtomicNumber(new AtomicLong(5));

		Game actualGame = gameService.play(playerInput);
		Assert.assertNotNull(actualGame);

		Assert.assertEquals(actualGame.getGameStatus().name(), GameStatus.PLAY.name());
		Assert.assertEquals(actualGame.getPlayer1().getOldNumber(), 6);
		Assert.assertEquals(actualGame.getPlayer1().getResultNumber(), 2);

		playerInput = createPlayerInput(player2Name, game.getId());
		actualGame = gameService.play(playerInput);
		Assert.assertNotNull(actualGame);

		Assert.assertEquals(actualGame.getGameStatus().name(), GameStatus.FINISH.name());
		Assert.assertEquals(actualGame.getPlayer2().getOldNumber(), 3);
		Assert.assertEquals(actualGame.getPlayer2().getResultNumber(), 1);
		String operation = "+1";
		Assert.assertEquals(
				player2Name + " WIN!!!. (" + operation + " to " + 2 + " = " + 3 + ")" + " with result number: " + 1,
				actualGame.getContent());

	}
	
	

	private Game createGame(String player1Name, String player2Name) {
		Game game = new Game(UUID.randomUUID().toString());
		Player player1 = new Player(player1Name);
		game.setGameStatus(GameStatus.JOIN);
		game.setPlayer1(player1);

		Player player2 = new Player(player2Name);
		game.setGameStatus(GameStatus.JOIN);
		game.setPlayer2(player2);

		return game;
	}

	private PlayerInput createPlayerInput(String playerName, String gameId) {
		PlayerInput playerInput = new PlayerInput();
		playerInput.setGameId(gameId);
		playerInput.setPlayerName(playerName);
		return playerInput;
	}

}
