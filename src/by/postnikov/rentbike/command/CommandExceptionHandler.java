package by.postnikov.rentbike.command;

import by.postnikov.rentbike.exception.ExceptionMessage;

public class CommandExceptionHandler {

	public static String takeLogicExceptionMessage(Exception e) {

		try {
			if (e.getCause() == null) {
				return ExceptionMessage.valueOf(e.getMessage()).message();
			} else {
				return ExceptionMessage.valueOf(e.getCause().getMessage()).message();
			}
		} catch (IllegalArgumentException e1) {
			return "";
		}
	}
}
