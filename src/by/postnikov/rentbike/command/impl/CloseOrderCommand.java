package by.postnikov.rentbike.command.impl;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ExceptionMessage;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class CloseOrderCommand implements Command{
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);
		router.setPagePath(PageConstant.REDIRECT_TO_HOME_PAGE);
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();
		
		HttpSession session = request.getSession(false);
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		try {	
			BigDecimal payment = userService.closeOrder(requestParameters);
			session.setAttribute(RequestParameter.PAYMENT.parameter(), payment.toString());
		} catch (ServiceException e) {
			if(CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
				logger.log(Level.ERROR, "Exception occurred while closing order, " + ConvertPrintStackTraceToString.convert(e));
				router.setPagePath(PageConstant.ERROR_PAGE);
			}else {
				if(ExceptionMessage.ORDER_NOT_EXIST.toString().equals(CommandExceptionHandler.takeLogicExceptionMessage(e))) {
					session.setAttribute(RequestParameter.ERROR.parameter(),  ExceptionMessage.ORDER_NOT_EXIST.message());
				}else {
					session.setAttribute(RequestParameter.ERROR.parameter(),
							CommandExceptionHandler.takeLogicExceptionMessage(e));
				}
			}

		}

		return router;
	}

}