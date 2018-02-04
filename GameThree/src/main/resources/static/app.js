var stompClient = null;

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
	if (connected) {
		$("#conversation").show();
	} else {
		$("#conversation").hide();
	}
	$("#conversations").html("");
}

function connect() {
	var socket = new SockJS('/gaming-websocket');
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		setConnected(true);
		console.log('Connected: ' + frame);
		stompClient.subscribe('/topic/gamings', function(result) {
			showResults(result);
		});
	});
}

function disconnect() {
	if (stompClient !== null) {
		stompClient.disconnect();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendNumber() {
	// JSON.stringify({'number' : $("#number").val()})
	stompClient.send("/app/game", {}, $("#number").val());
}

function showResults(result) {
	$("#conversations").append(
			"<tr><td>Player Name: " + JSON.parse(result.body).name
					+ ", Add number " + JSON.parse(result.body).number
					+ ", with result " + JSON.parse(result.body).resultNumber
					+ "</td></tr>");
}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});
	$("#disconnect").click(function() {
		disconnect();
	});
	$("#send").click(function() {
		sendNumber();
	});
});