package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class TakeAllUserCommand implements Command {
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		try {
			request.setAttribute(RequestParameter.ORDER_LIST.parameter(), userService.takeAllUsers());
			router.setPagePath(PageConstant.ADMIN_PAGE);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "take all userd error, " + e);
			router.setPagePath(PageConstant.ERROR_PAGE);
		}
		
		return router;
	}

}
