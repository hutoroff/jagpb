package ru.hutoroff.jagpb.bot.exceptions;

public class UnknownOptionsException extends Exception {

	public UnknownOptionsException() {
	}

	public UnknownOptionsException(String message) {
		super(message);
	}

	public UnknownOptionsException(String message, Throwable cause) {
		super(message, cause);
	}
}
