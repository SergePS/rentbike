package by.postnikov.rentbike.command.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.User;
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

		User user = new User();

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		char[] password = request.getParameter(RequestParameter.PASSWORD.parameter()).toCharArray();

		String errorParameterName;
		try {
			errorParameterName = userService.register(requestParameters, password, user);
			
			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}
			
			if (errorParameterName.isEmpty()) {
				router.setRoute(RouteType.REDIRECT);
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorParameterName);
				request.setAttribute(RequestParameter.USER_WRONG.parameter(), user);
				request.setAttribute(RequestParameter.LOGIN_MENU.parameter(), false);
				router.setRoute(RouteType.FORWARD);
			}
			
			router.setPagePath(PageConstant.REDIRECT_TO_LOGIN_PAGE);
			
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "An error occured while the user was creating, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}
		
		return router;
	}

}
