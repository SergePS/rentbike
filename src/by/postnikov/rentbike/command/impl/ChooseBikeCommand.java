package by.postnikov.rentbike.command.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class ChooseBikeCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		String bikeIdString = request.getParameter(RequestParameter.BIKE_ID.parameter());

		try {
			Bike bike = bikeService.takeBikeByID(bikeIdString);
			if (bike != null) {
				request.setAttribute(RequestParameter.BIKE.parameter(), bike);
			}
			ParkingService parkingService = serviceFactory.getParkingService();
			List<Parking> parkingList = parkingService.takeAllParking();
			request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
			router.setPagePath(PageConstant.BIKE_PURCHASE_PAGE);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Get bike by id error");
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
