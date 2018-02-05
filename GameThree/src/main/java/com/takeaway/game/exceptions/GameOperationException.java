package com.takeaway.game.exceptions;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
public class GameOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GameOperationException() {
		super();
	}

	public GameOperationException(String message) {
		super(message);
	}

	public GameOperationException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameOperationException(Throwable cause) {
		super(cause);
	}

}
