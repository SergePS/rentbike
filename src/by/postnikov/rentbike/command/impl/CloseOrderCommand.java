package by.postnikov.rentbike.command.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageMessage;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class CloseOrderCommand implements Command{
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();
		
		HttpSession session = request.getSession(false);
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		try {	
			String paymentOrErrorMessage = userService.closeOrder(requestParameters);
			
			if(PageMessage.VALIDATION_ERROR.message().equals(paymentOrErrorMessage)) {
				router.setPagePath(PageConstant.ERROR_PAGE);
				session.setAttribute(RequestParameter.ERROR.parameter(), PageMessage.VALIDATION_ERROR.message());
				return router;
			}
			
			if(PageMessage.ORDER_NOT_EXIST.message().equals(paymentOrErrorMessage)) {
				session.setAttribute(RequestParameter.ERROR.parameter(),  PageMessage.ORDER_NOT_EXIST.message());
			}else {
				session.setAttribute(RequestParameter.PAYMENT.parameter(), paymentOrErrorMessage);
			}
			router.setPagePath(PageConstant.REDIRECT_TO_HOME_PAGE);

		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Close order error, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}