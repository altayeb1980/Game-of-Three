package com.takeaway.game.model;

import java.io.Serializable;

/**
 * 
 * @author Al-Tayeb_Saadeh
 *
 */
public class Player implements Serializable{
	private static final long serialVersionUID = 1L;
	private long oldNumber;
	private long resultNumber;
	private final String name;

	public Player(final String name) {
		this.name = name;
	}

	public long getOldNumber() {
		return oldNumber;
	}

	public String getName() {
		return name;
	}

	public long getResultNumber() {
		return resultNumber;
	}

	public void setOldNumber(long oldNumber) {
		this.oldNumber = oldNumber;
	}

	public void setResultNumber(long resultNumber) {
		this.resultNumber = resultNumber;
	}

	@Override
	public String toString() {
		return name + " has a number " + oldNumber + " with result " + resultNumber;
	}
}
