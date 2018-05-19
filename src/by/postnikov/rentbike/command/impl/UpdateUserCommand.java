package by.postnikov.rentbike.command.impl;

import java.util.List;
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
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.command.util.AddTimeParameterToRequest;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class UpdateUserCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();
		
		HttpSession session = request.getSession();
		
		User user = (User)session.getAttribute(SessionParameter.USER.parameter());

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		String errorParameterName;
		try {
			errorParameterName = userService.userUpdate(requestParameters, user);
			
			if (errorParameterName.isEmpty()) {
				request.setAttribute(RequestParameter.MESSAGE.parameter(), MessagePage.PROFILE_CHANGED.message());
				session.setAttribute(SessionParameter.USER.parameter(), user);
			}else {
				if (MessagePage.VALIDATION_ERROR.message().equals(errorParameterName)) {
					request.setAttribute(RequestParameter.ERROR.parameter(), MessagePage.VALIDATION_ERROR.message());
				}
				if (MessagePage.USER_DUBLICATE_ERROR.message().equals(errorParameterName)) {
					request.setAttribute(RequestParameter.ERROR.parameter(), MessagePage.USER_DUBLICATE_ERROR.message());
				}
				request.setAttribute(RequestParameter.LOGIN_MENU.parameter(), false);
			}
			router.setPagePath(user.getRole().getHomePage());	
			
			BikeOrder bikeOrder = userService.findOpenOrder(user);
			request.setAttribute(RequestParameter.BIKE_ORDER.parameter(), bikeOrder);
			if (bikeOrder != null) {
				AddTimeParameterToRequest.addParam(request, bikeOrder.getStartTime());

				ParkingService parkingService = serviceFactory.getParkingService();
				List<Parking> parkingList = parkingService.takeAllParking();
				request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
			}
			
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Update user error, " + e);
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
