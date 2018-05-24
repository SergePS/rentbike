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
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class GoToAddBikePageCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		try {
			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);

			List<Brand> brandList = bikeService.takeAllBrand();
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);

			String bikeIdString = request.getParameter(RequestParameter.BIKE_ID.parameter());
			if (bikeIdString != null) {
				String bikeId = request.getParameter(RequestParameter.BIKE_ID.parameter());
				Bike bike = bikeService.takeBikeByID(bikeId);
				if(bike!=null) {
					request.setAttribute(RequestParameter.BIKE.parameter(), bike);
				}
			}
			router.setPagePath(PageConstant.ADD_BIKE_PAGE);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Add bike exception, " + e.getMessage());
			router.setPagePath(PageConstant.ERROR_PAGE);
		}
		
		return router;
	}

}
