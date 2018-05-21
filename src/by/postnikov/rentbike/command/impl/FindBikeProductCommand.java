package by.postnikov.rentbike.command.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.command.PageInfoHandler;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class FindBikeProductCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		
		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();
		ParkingService parkingService = serviceFactory.getParkingService();

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);
		
		PageInfo pageInfo = PageInfoHandler.pageInfoInit(request);

		try {
			List<BikeProduct> bikeProductList = new ArrayList<>();
			String errorMessage = bikeService.findBikeProduct(requestParameters, bikeProductList, pageInfo);
			if(errorMessage.isEmpty()) {
				request.setAttribute(RequestParameter.BIKE_PRODUCT_LIST.parameter(), bikeProductList);
				RequestParameterHandler.addParamToReques(request);
				PageInfoHandler.handleAndAddToSession(pageInfo, request, bikeProductList);
			}else {
				request.setAttribute(RequestParameter.ERROR.parameter(), errorMessage);
			}
			
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
