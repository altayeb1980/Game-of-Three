package com.takeaway.game;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.takeaway.game.model.Game;
import com.takeaway.game.repository.GameRepository;

public class GameRepositoryTest extends BaseGameTest{

	private GameRepository gameRepository;

	@Before
	public void setup() {
		gameRepository = new GameRepository();
	}

	@Test
	public void testAddOrUpdateGame() {
		Game gameForPlayer1 = createGame("altayeb", null);
		gameRepository.addOrUpdateGame(gameForPlayer1);
		Assert.assertTrue(gameRepository.lisGames().size() == 1);
		Game gameForPlayer2 = createGame(null, "emmy");
		gameRepository.addOrUpdateGame(gameForPlayer2);
	}

	@Test
	public void testGetFirstOpenGame() {
		Game game = createGame("altayeb", null);
		gameRepository.addOrUpdateGame(game);
		Game firstOpenGame = gameRepository.getFirstOpenGame();
		Assert.assertEquals(gameId, firstOpenGame.getId());
		Assert.assertEquals("altayeb", firstOpenGame.getPlayer1().getName());
	}

	@Test
	public void testGetGameById() {
		Game game = createGame("altayeb", "emmy");
		gameRepository.addOrUpdateGame(game);

		Game actualGame = gameRepository.getGameById(gameId);

		Assert.assertEquals("altayeb", actualGame.getPlayer1().getName());
		Assert.assertEquals("emmy", actualGame.getPlayer2().getName());

	}

	

}
