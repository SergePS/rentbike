package by.postnikov.rentbike.command.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.PageInfoHandler;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.PageInfo;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class FindOrderByParametersCommand implements Command{
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setPagePath(PageConstant.ORDER_REPORT_PAGE);
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		PageInfo pageInfo = PageInfoHandler.pageInfoInit(request);
		RequestParameterHandler.addParamToRequest(request);
		
		try {
			List<BikeOrder> bikeOrderList = userService.findOrder(requestParameters, pageInfo);
			request.setAttribute(RequestParameter.BIKE_ORDER_LIST.parameter(), bikeOrderList);
			PageInfoHandler.handleAndAddToSession(pageInfo, request, bikeOrderList);
		} catch (ServiceException e) {
			if (CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
				logger.log(Level.ERROR, "Exception occurred while find orders, " + ConvertPrintStackTraceToString.convert(e));
				router.setPagePath(PageConstant.ERROR_PAGE);
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(),
						CommandExceptionHandler.takeLogicExceptionMessage(e));
				pageInfo = PageInfoHandler.pageInfoInit(request);
				
				//remove the parameter to make paging menu unavailable.
				HttpSession session = request.getSession(true);
				session.removeAttribute(SessionParameter.PAGE_INFO.parameter());
			}
		}
		
		return router;
	}

}