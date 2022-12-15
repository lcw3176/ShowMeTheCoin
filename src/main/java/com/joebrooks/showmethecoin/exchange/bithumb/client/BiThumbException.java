package com.joebrooks.showmethecoin.exchange.bithumb.client;

public class BiThumbException extends RuntimeException {

	public BiThumbException(String msg) {
		super(msg);
	}

	public BiThumbException(String msg, Throwable e) {
		super(msg, e);
	}
}
