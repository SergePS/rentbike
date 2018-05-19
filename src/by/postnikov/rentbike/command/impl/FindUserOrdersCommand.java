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
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ServiceFactory;
import by.postnikov.rentbike.service.UserService;

public class FindUserOrdersCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		UserService userService = serviceFactory.getUserService();

		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute(SessionParameter.USER.parameter());

		try {
			List<BikeOrder> bikeOrderList = userService.findAllOrderByUser(user);
			if(!bikeOrderList.isEmpty()) {
				request.setAttribute(RequestParameter.BIKE_ORDER_LIST.parameter(), bikeOrderList);
			}
			router.setPagePath(user.getRole().getHomePage());
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Take all user orders error, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
