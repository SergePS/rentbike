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
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddBikeProductCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		Router router = new Router();
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		try {
			List<BikeProduct> bikeProductList = new ArrayList<>();
			String errorMessage = bikeService.addBikeProduct(requestParameters, bikeProductList);
			if(errorMessage.isEmpty()) {
				HttpSession session = request.getSession(false);
				session.setAttribute(RequestParameter.BIKE_PRODUCT_LIST.parameter(), bikeProductList);
				router.setRoute(RouteType.REDIRECT);
				router.setPagePath(PageConstant.REDIRECT_TO_BIKE_PURCHASE_PAGE);
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorMessage);
				RequestParameterHandler.addParamToReques(request);
				
				ParkingService parkingService = serviceFactory.getParkingService();
				List<Parking> parkingList = parkingService.takeAllParking();
				request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
				
				router.setRoute(RouteType.FORWARD);
				router.setPagePath(PageConstant.BIKE_PURCHASE_PAGE);
			}
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Add bike product error" + ConvertPrintStackTraceToString.convert(e));
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}
}
