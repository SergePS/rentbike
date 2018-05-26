package by.postnikov.rentbike.command.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.PageMessage;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class RegisterUserCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		char[] password = request.getParameter(RequestParameter.PASSWORD.parameter()).toCharArray();

		try {
			userService.register(requestParameters, password);
			
			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}

			router.setRoute(RouteType.REDIRECT);
			router.setPagePath(PageConstant.REDIRECT_TO_LOGIN_PAGE);
			
			HttpSession session = request.getSession(true);
			session.setAttribute(SessionParameter.MESSAGE.parameter(), PageMessage.USER_ADDED.message());
			
		} catch (ServiceException e) {
			if (CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
				logger.log(Level.ERROR,
						"An error occured while the user was creating, " + ConvertPrintStackTraceToString.convert(e));
				router.setPagePath(PageConstant.ERROR_PAGE);
			} else {
				request.setAttribute(RequestParameter.ERROR.parameter(), CommandExceptionHandler.takeLogicExceptionMessage(e));
				RequestParameterHandler.addParamToRequest(request);
				request.setAttribute(RequestParameter.LOGIN_MENU.parameter(), false);
				router.setRoute(RouteType.FORWARD);
				router.setPagePath(PageConstant.LOGIN_PAGE);
			}

		}

		return router;
	}

}
