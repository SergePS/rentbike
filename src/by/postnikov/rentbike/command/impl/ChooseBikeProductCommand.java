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
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class ChooseBikeProductCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		try {
			BikeProduct bikeProduct = new BikeProduct();
			String bikeProductIdString = request.getParameter(RequestParameter.BIKE_PRODUCT_ID.parameter());
			bikeService.takeBikeProductById(bikeProductIdString, bikeProduct);

			HttpSession session = request.getSession(false);
			session.setAttribute(SessionParameter.BIKE_PRODUCT.parameter(), bikeProduct);

			router.setPagePath(PageConstant.ORDER_PAGE);
		} catch (NumberFormatException | ServiceException e) {
			logger.log(Level.ERROR, "Get bike product by id error, " + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;

	}

}
