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
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class GoToBikeProductCatalogPageCommand implements Command {

	private static Logger logger = LogManager.getLogger();
	
	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		ParkingService parkingService = serviceFactory.getParkingService();
		BikeService bikeService = serviceFactory.getBikeService();

		try {
			List<Parking> parkingList = parkingService.takeAllParking();
			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			List<Brand> brandList = bikeService.takeAllBrand();

			request.setAttribute(RequestParameter.PARKING_LIST.parameter(), parkingList);
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);
			
			router.setPagePath(PageConstant.BIKE_PRODUCT_CATALOG_PAGE);

		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Get data error, " + e.getMessage());
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
