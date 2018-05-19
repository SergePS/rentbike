package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.controller.Router;

public class ChangeLocalizationCommand implements Command{

	@Override
	public Router execute(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		
		Router router = new Router();
		
		boolean loginMenu;
		if(request.getParameter(RequestParameter.LOGIN_MENU.parameter()).isEmpty()) {
			loginMenu = true;
		}else {
			loginMenu = Boolean.valueOf(request.getParameter(RequestParameter.LOGIN_MENU.parameter()));
		}	
		request.setAttribute(RequestParameter.LOGIN_MENU.parameter(), loginMenu);
		
		session.setAttribute(SessionParameter.LOCAL.parameter(), request.getParameter(SessionParameter.LANGUAGE.parameter()));
		
		router.setPagePath(PageConstant.LOGIN_PAGE);
		return router;
	}

}
