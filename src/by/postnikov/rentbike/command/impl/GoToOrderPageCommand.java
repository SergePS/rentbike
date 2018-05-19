package by.postnikov.rentbike.command.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.command.util.AddTimeParameterToRequest;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class GoToOrderPageCommand implements Command{
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();
		
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(SessionParameter.USER.parameter());
		
		try {
			BikeOrder bikeOrder = userService.findOpenOrder(user);
			
			if (bikeOrder != null) {
				request.setAttribute(RequestParameter.BIKE_ORDER.parameter(), bikeOrder);
				AddTimeParameterToRequest.addParam(request, bikeOrder.getStartTime());
				
				ParkingService parkingService = serviceFactory.getParkingService();
				List<Parking> parkingList = parkingService.takeAllParking();
				request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
				
				router.setPagePath(PageConstant.USER_PAGE);
				return router;
			}
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "find open order error, " + ConvertPrintStackTraceToString.convert(e));
		}
		
		router.setPagePath(PageConstant.ORDER_PAGE);
		
		return router;
	}

}
