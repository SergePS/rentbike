package by.postnikov.rentbike.command;

import javax.servlet.http.HttpServletRequest;

import by.postnikov.rentbike.controller.Router;

public interface Command {
	
	public Router execute(HttpServletRequest request);

}
