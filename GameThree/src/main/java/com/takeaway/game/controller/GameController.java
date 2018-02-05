package com.takeaway.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.takeaway.game.model.Game;
import com.takeaway.game.model.PlayerInput;
import com.takeaway.game.service.GameService;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
@Controller
public class GameController {

	private static final String GAME_DESTINATION = "/topic/game";
	private static final String GAME_PLAY_MAPPING = "/game.play";
	private static final String GAME_JOIN_GAME_MAPPING = "/game.joinGame";
	private static final String GAME_START_GAME_MAPPING = "/game.startGame";

	@Autowired
	private GameService gameService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping(GAME_PLAY_MAPPING)
	public void play(PlayerInput playerInput) throws Exception {
		Game game = gameService.play(playerInput);
		simpMessagingTemplate.convertAndSend(GAME_DESTINATION + "/" + playerInput.getGameId(), game);
	}

	@MessageMapping(GAME_JOIN_GAME_MAPPING)
	@SendTo(GAME_DESTINATION)
	public Game joinGame(PlayerInput playerInput) {
		return gameService.jointGame(playerInput.getPlayerName());
	}

	@MessageMapping(GAME_START_GAME_MAPPING)
	@SendTo(GAME_DESTINATION)
	public Game startGame(PlayerInput playerInput) {
		return gameService.startGame(playerInput);
	}

	// @MessageMapping(GAME_NOTIFY_PLAYER_JOIN_MAPPING)
	// public void notifyPlayerJoint(String gameId) {
	// Game game = gameService.getGameById(gameId);
	// simpMessagingTemplate.convertAndSend(GAME_DESTINATION + "/" + gameId, game);
	// }
}
