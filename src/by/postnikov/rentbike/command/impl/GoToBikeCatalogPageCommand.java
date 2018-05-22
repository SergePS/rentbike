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
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class GoToBikeCatalogPageCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();
		
		HttpSession session = request.getSession(false);
		
		session.setAttribute(SessionParameter.BIKE_CATALOG_WITH_CHOICE.parameter(), request.getParameter(RequestParameter.BIKE_CATALOG_WITH_CHOICE.parameter()));

		try {
			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			List<Brand> brandList = bikeService.takeAllBrand();
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);
			
			router.setPagePath(PageConstant.BIKE_CATALOG_PAGE);
		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Get bike tipe or brand exception, " + e.getMessage());
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
