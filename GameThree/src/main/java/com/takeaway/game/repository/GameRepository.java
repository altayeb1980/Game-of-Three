package com.takeaway.game.repository;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.takeaway.game.model.Game;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@Repository
public class GameRepository {

	private Set<Game> games = new HashSet<Game>();

	public void addOrUpdateGame(Game game) {
		games.remove(game);
		games.add(game);
	}

	public Set<Game> lisGames() {
		return games;
	}

	public Game getFirstOpenGame() {
		return games.stream().filter(g -> g.isOpen()).findFirst().orElse(null);
	}

	public Game getGameById(String gameId) {
		return games.stream().filter(g -> g.getId().equals(gameId)).findFirst().orElse(null);
	}

	public void clearGame() {
		games.clear();
	}

}
