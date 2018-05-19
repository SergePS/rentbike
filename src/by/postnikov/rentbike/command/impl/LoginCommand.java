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
import by.postnikov.rentbike.entity.UserRole;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class LoginCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		HttpSession session = request.getSession();

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		char[] password = request.getParameter(RequestParameter.PASSWORD.parameter()).toCharArray();

		User user = new User();

		try {
			String errorParameterName;
			errorParameterName = userService.login(requestParameters, password, user);

			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}

			if (errorParameterName.isEmpty()) {

				if (user.getId() == 0) {
					request.setAttribute(RequestParameter.ERROR.parameter(), MessagePage.LOGIN_PASSW.message());
					request.setAttribute(RequestParameter.USER_WRONG.parameter(), user);
					router.setPagePath(PageConstant.LOGIN_PAGE);
					return router;
				}

				if (UserRole.USER.equals(user.getRole())) {

					BikeOrder bikeOrder = userService.findOpenOrder(user);
					if (bikeOrder != null) {
						request.setAttribute(RequestParameter.BIKE_ORDER.parameter(), bikeOrder);
						AddTimeParameterToRequest.addParam(request, bikeOrder.getStartTime());

						ParkingService parkingService = serviceFactory.getParkingService();
						List<Parking> parkingList = parkingService.takeAllParking();
						request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
					}
				}

				router.setPagePath(user.getRole().getHomePage());
				session.setAttribute(SessionParameter.USER.parameter(), user);
			} else {
				if (MessagePage.VALIDATION_ERROR.message().equals(errorParameterName)) {
					request.setAttribute(RequestParameter.ERROR.parameter(), MessagePage.VALIDATION_ERROR.message());
					router.setPagePath(PageConstant.LOGIN_PAGE);
				}
			}

		} catch (ServiceException e) {
			logger.log(Level.ERROR, "An exception occurred while get user data, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
			return router;
		}

		return router;
	}

}
