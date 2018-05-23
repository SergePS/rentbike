package by.postnikov.rentbike.command.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddParkingCommand implements Command{
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		ParkingService parkingService = serviceFactory.getParkingService();
		
		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		try {
			String errorMessage = parkingService.addParking(requestParameters);
			
			if(errorMessage.isEmpty()) {
				router.setPagePath(PageConstant.REDIRECT_TO_PARKING_PAGE);
				HttpSession session = request.getSession(true);
				session.setAttribute(RequestParameter.MESSAGE.parameter(), MessagePage.PARKING_ADDED.message());
			}else {
				router.setRoute(RouteType.FORWARD);
				router.setPagePath(PageConstant.ADD_PARKING_PAGE);
				request.setAttribute(RequestParameter.ERROR.parameter(), errorMessage);
			}	
			
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "An error occurred while adding the parking, " + ConvertPrintStackTraceToString.convert(e));
			router.setRoute(RouteType.FORWARD);
			router.setPagePath(PageConstant.ERROR_PAGE);
		}
		
		return router;
	}
	
}
