package by.postnikov.rentbike.command.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddBrandCommand implements Command {

	private static Logger logger = LogManager.getLogger();
	
	private final static String IS_ADD_FORM_PARAM = "isAddForm";

	@Override
	public Router execute(HttpServletRequest request) {
		
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();
		
		Router router = new Router();

		Boolean isAddForm = Boolean.valueOf(request.getParameter(IS_ADD_FORM_PARAM));

		if(isAddForm){
			router.setPagePath(PageConstant.BRAND_PAGE);
			return router;
		}
		
		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		Brand brand = new Brand();

		try {
			String errorMessage = bikeService.addBrand(requestParameters, brand);
			if (errorMessage.isEmpty()) {
				List<Brand> brandList = bikeService.takeAllBrand();
				request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);
				
				List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
				request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);
				
				router.setPagePath(PageConstant.ADD_BIKE_PAGE);					
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorMessage);
				router.setPagePath(PageConstant.BRAND_PAGE);
			}

		} catch (ServiceException e) {
			logger.log(Level.ERROR, "Add bike exception, " + e.getMessage());
			router.setPagePath(PageConstant.ERROR_PAGE);
		}

		return router;
	}

}
