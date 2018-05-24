package by.postnikov.rentbike.command.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.command.PageInfoHandler;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.SessionParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class FindBikeCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();
		router.setPagePath(PageConstant.BIKE_CATALOG_PAGE);

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		Map<String, String> requestParameters = RequestParameterHandler.requestParamToMap(request);

		PageInfo pageInfo = PageInfoHandler.pageInfoInit(request);

		try {
			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			List<Brand> brandList = bikeService.takeAllBrand();
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);
			RequestParameterHandler.addParamToReques(request);
						
			List<Bike> bikeList = bikeService.findBike(requestParameters, pageInfo);
			request.setAttribute(RequestParameter.BIKE_LIST.parameter(), bikeList);
			
			PageInfoHandler.handleAndAddToSession(pageInfo, request, bikeList);
		} catch (ServiceException e) {
			if (CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
				logger.log(Level.ERROR, "Find bike error, " + e.getMessage());
				router.setPagePath(PageConstant.ERROR_PAGE);
			} else {
				request.setAttribute(RequestParameter.ERROR.parameter(),
						CommandExceptionHandler.takeLogicExceptionMessage(e));
				pageInfo = PageInfoHandler.pageInfoInit(request);
				
				//remove the parameter to make paging menu unavailable.
				HttpSession session = request.getSession(true);
				session.removeAttribute(SessionParameter.PAGE_INFO.parameter());
			}
		}

		return router;
	}

}
