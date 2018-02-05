package com.takeaway.game.model;

import java.io.Serializable;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
public class PlayerInput implements Serializable{
	private static final long serialVersionUID = 3173532996220750854L;
	private String playerName;
	private String gameId;

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
