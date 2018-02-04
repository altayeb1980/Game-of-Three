package com.takeaway.game;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import com.takeaway.game.Game.GameStatus;


/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class GameThreeTest {

	@Mock
	private GameRepository gameRepository;

	@InjectMocks
	private GameService gameService;

	@Test
	public void TestJoinGame_WhenNoPlayerExitYet() {
		String playerName = "altayeb";
		Mockito.when(gameRepository.getFirstOpenGame()).thenReturn(null);
		Game game = createGame(playerName);
		Game newGame = gameService.jointGame(playerName);
		Mockito.verify(gameRepository).addOrUpdateGame(newGame);
		Assert.assertEquals(game.getPlayer1().getName(), newGame.getPlayer1().getName());
		Assert.assertTrue(newGame.getPlayer1().getOldNumber() == 0);
		Assert.assertEquals(newGame.getGameStatus().name(), GameStatus.JOIN.name());
	}

	@Test
	public void TestJoinGame_WhenPlayerOneExit() {
		String playerName = "Emmy";
		Game game = createGame(playerName);
		Mockito.when(gameRepository.getFirstOpenGame()).thenReturn(game);
		Game newGame = gameService.jointGame(playerName);
		Mockito.verify(gameRepository).addOrUpdateGame(newGame);
		Assert.assertEquals(game.getPlayer2().getName(), newGame.getPlayer2().getName());
		Assert.assertTrue(newGame.getPlayer2().getOldNumber() == 0);
		Assert.assertEquals(newGame.getGameStatus().name(), GameStatus.JOIN.name());
	}
	
	
	@Test
	public void TestStartGame() {
		String playerName = "altayeb";
		Game game = createGame(playerName);
		Mockito.when(gameRepository.getGameById(Mockito.any(String.class))).thenReturn(game);
		
		PlayerInput playerInput = new PlayerInput();
		playerInput.setGameId(game.getId());
		playerInput.setPlayerName(playerName);
		Game actualGame = gameService.startGame(playerInput);
		
		Assert.assertEquals(actualGame.getPlayer1().getName(), game.getPlayer1().getName());
		Assert.assertEquals(actualGame.getGameStatus().name(), GameStatus.START.name());
		Assert.assertTrue(actualGame.getPlayer1().getOldNumber() > 0);
	}
	
	

	private Game createGame(String playerName) {
		Game game = new Game(UUID.randomUUID().toString());
		Player player = new Player(playerName);
		game.setGameStatus(GameStatus.JOIN);
		game.setPlayer1(player);
		return game;
	}

}
