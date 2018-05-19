package by.postnikov.rentbike.command.impl;

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
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class CreateOrderCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		HttpSession session = request.getSession();
		BikeProduct bikeProduct = (BikeProduct) session.getAttribute(SessionParameter.BIKE_PRODUCT.parameter());
		session.removeAttribute(SessionParameter.BIKE_PRODUCT.parameter());

		User user = (User) session.getAttribute(SessionParameter.USER.parameter());

		try {
			BikeOrder bikeOrder = userService.findOpenOrder(user);
			if (bikeOrder != null) {
				request.setAttribute(RequestParameter.BIKE_ORDER.parameter(), bikeOrder);
				router.setPagePath(PageConstant.USER_PAGE);
				return router;
			}

			bikeOrder = userService.createOrder(user, bikeProduct);
			request.setAttribute(RequestParameter.BIKE_ORDER.parameter(), bikeOrder);
			AddTimeParameterToRequest.addParam(request, bikeOrder.getStartTime());
			router.setPagePath(PageConstant.REDIRECT_TO_HOME_PAGE);

		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Create order error, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}
			
		return router;
	}

}
