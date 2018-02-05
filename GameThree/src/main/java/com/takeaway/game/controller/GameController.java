package com.takeaway.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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

	private static final String GAME_DESTINATION_ERROR = "/topic/error";
	private static final String GAME_DESTINATION = "/topic/game";
	private static final String GAME_PLAY_MAPPING = "/game.play";
	private static final String GAME_JOIN_GAME_MAPPING = "/game.joinGame";
	private static final String GAME_START_GAME_MAPPING = "/game.startGame";

	@Autowired
	private GameService gameService;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@MessageMapping(GAME_PLAY_MAPPING)
	public void play(PlayerInput playerInput) {
		Game game = gameService.play(playerInput);
		simpMessagingTemplate.convertAndSend(GAME_DESTINATION + "/" + playerInput.getGameId(), game);
	}

	@MessageMapping(GAME_JOIN_GAME_MAPPING)
	public void joinGame(PlayerInput playerInput, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("playerName", playerInput.getPlayerName());
		Game game = gameService.jointGame(playerInput.getPlayerName());
		simpMessagingTemplate.convertAndSend(GAME_DESTINATION, game);
	}

	@MessageMapping(GAME_START_GAME_MAPPING)
	public void startGame(PlayerInput playerInput) {
		Game game = gameService.startGame(playerInput);
		simpMessagingTemplate.convertAndSend(GAME_DESTINATION, game);
	}

	@MessageExceptionHandler
	public void handleException(RuntimeException exp, SimpMessageHeaderAccessor headerAccessor) {
		String playerName = headerAccessor.getSessionAttributes().get("playerName")+"";
		simpMessagingTemplate.convertAndSend(GAME_DESTINATION_ERROR+"/"+playerName, exp.getMessage());
	}
}
