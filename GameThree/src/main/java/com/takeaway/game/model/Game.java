package com.takeaway.game.model;

import java.io.Serializable;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
public class Game implements Serializable{
	private static final long serialVersionUID = -1995405342202971525L;
	private final String id;
	private Player player1;
	private Player player2;
	private String content;
	private String currentPlayerName;

	private GameStatus gameStatus;

	public enum GameStatus {
		FINISH, JOIN, PLAY, START
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Game(final String id) {
		this.id = id;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}

	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isOpen() {
		return player1 != null && player2 == null;
	}

	public boolean isClose() {
		return player1 != null && player2 != null;
	}
	
	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getCurrentPlayerName() {
		return currentPlayerName;
	}

	public void setCurrentPlayerName(String currentPlayerName) {
		this.currentPlayerName = currentPlayerName;
	}

}
