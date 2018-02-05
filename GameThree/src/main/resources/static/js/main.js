var conversationsArea = document.querySelector('#conversationsArea');

var stompClient = null;
var playerName = null;
var gameId = null;

function inceptNumber(payload) {
	var game = JSON.parse(payload.body);
	var inceptsStr = game.player1.name + ' incepts randome number '
			+ game.player1.oldNumber;
	addConversation(playerNameJoin);
}

function startGame() {
	var playerInput = {
		playerName : playerName,
		gameId : gameId
	};

	stompClient.send("/app/game.startGame", {}, JSON.stringify(playerInput))
}

function connect() {
	playerName = document.querySelector('#name').value.trim();

	if (playerName) {
		$('#playerNameForm').addClass('hidden');
		$('#game-page').removeClass('hidden');

		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, onConnected, onError);

	}
}

function onConnected() {
	var playerInput = {
		playerName : playerName,
		gameId : gameId
	};

	stompClient.subscribe('/topic/game', initGame);
	stompClient.send("/app/game.joinGame", {}, JSON.stringify(playerInput));
	$('.connecting').addClass('hidden');
}

function onError(error) {
	$('.connecting').textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	$('.connecting').style.color = 'red';
}

function play() {

	var playerInput = {
		playerName : playerName,
		gameId : gameId
	};

	$('#send').hide();
	stompClient.send("/app/game.play", {}, JSON.stringify(playerInput));

}

function onJoinGame(payload) {
	if (payload == null)
		return;

	var game = JSON.parse(payload.body);

	if (game.gameStatus == 'PLAY') {
		addConversation(game.content);
		if (game.currentPlayerName != playerName) {
			$('#send').show();
		}
	} else if (game.gameStatus == 'FINISH') {
		addConversation(game.content);
		$('#send').hide();
	}
}

function initGame(payload) {
	var game = JSON.parse(payload.body);
	gameId = game.id;

	if (game.gameStatus == 'JOIN') {
		var playerNameJoin = "";
		if (game.player1 != null) {
			playerNameJoin = game.player1.name;
		}
		if (game.player2 != null) {
			playerNameJoin = game.player2.name;
		}
		playerNameJoin = playerNameJoin + ' joined';

		addConversation(playerNameJoin);
		$('#send').hide();

		if (game.player1.name == playerName && game.player2 != null) {
			$('#start').show();
		}

	} else if (game.gameStatus == 'START') {
		var inceptsStr = "";
		if (game.player1.oldNumber != 0) {
			inceptsStr = game.player1.name
					+ ' start the game with incepts randome number '
					+ game.player1.oldNumber;
		}
		addConversation(inceptsStr);
		if (game.player1.name == playerName && game.player2 != null) {
			$('#start').hide();
			$('#send').hide();
		} else {
			$('#send').show();
		}
		stompClient.subscribe('/topic/game/' + gameId, onJoinGame);
	}
}

function addConversation(conversationText) {
	var gameElement = document.createElement('li');
	var textElement = document.createElement('p');
	var messageText = document.createTextNode(conversationText);
	textElement.appendChild(messageText);
	gameElement.appendChild(textElement);
	$('#conversationsArea').append(gameElement);
	$('#conversationsArea').scrollTop = $('#conversationsArea').scrollHeight;
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});

	$("#playerNameForm").click(function() {
		connect();
	});

});
