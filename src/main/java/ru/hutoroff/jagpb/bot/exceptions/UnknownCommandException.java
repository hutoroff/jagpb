package ru.hutoroff.jagpb.bot.exceptions;

public class UnknownCommandException extends Exception {
	public UnknownCommandException() {
	}

	public UnknownCommandException(String message) {
		super(message);
	}
}
