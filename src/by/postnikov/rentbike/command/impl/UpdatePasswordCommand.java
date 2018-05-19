package by.postnikov.rentbike.command.impl;

import java.util.List;

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
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class UpdatePasswordCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(SessionParameter.USER.parameter());

		char[] currentPassword = request.getParameter(RequestParameter.CURRENT_PASSWORD.parameter()).toCharArray();
		char[] password = request.getParameter(RequestParameter.PASSWORD.parameter()).toCharArray();

		String errorParameterName;
		try {
			errorParameterName = userService.updatePassword(currentPassword, password, user);
			
			for (int i = 0; i < currentPassword.length; i++) {
				currentPassword[i] = 0;
			}
			
			for (int i = 0; i < password.length; i++) {
				password[i] = 0;
			}

			if (errorParameterName.isEmpty()) {
				request.setAttribute(RequestParameter.MESSAGE.parameter(), MessagePage.PASSWORD_CHANGED.message());
			} else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorParameterName);
			}
			request.setAttribute(RequestParameter.LOGIN_MENU.parameter(), false);
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
			logger.log(Level.ERROR, "Update password error, " + e);
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
