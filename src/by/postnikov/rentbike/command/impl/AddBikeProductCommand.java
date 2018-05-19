package by.postnikov.rentbike.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.RouteType;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddBikeProductCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		Router router = new Router();
		router.setRoute(RouteType.REDIRECT);

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		try {
			List<BikeProduct> bikeProductList = new ArrayList<>();
			bikeService.addBikeProduct(requestParameters, bikeProductList);

			HttpSession session = request.getSession(false);
			session.setAttribute(RequestParameter.BIKE_PRODUCT_LIST.parameter(), bikeProductList);
			router.setPagePath(PageConstant.REDIRECT_TO_BIKE_PURCHASE_PAGE);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Add bike product error" + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}
}
