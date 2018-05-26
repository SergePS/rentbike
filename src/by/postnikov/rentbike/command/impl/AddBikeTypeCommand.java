package by.postnikov.rentbike.command.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.command.util.RequestParameterHandler;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.ServiceFactory;

public class AddBikeTypeCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {
		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		BikeService bikeService = serviceFactory.getBikeService();

		Router router = new Router();

		Boolean isAddForm = Boolean.valueOf(request.getParameter(RequestParameter.IS_ADD_FORM_PARAM.parameter()));

		if (isAddForm) {
			router.setPagePath(PageConstant.BIKE_TYPE_PAGE);
			return router;
		}

		String bikeType = request.getParameter(RequestParameter.BIKE_TYPE.parameter());

		try {
			bikeService.addBikeType(bikeType);

			List<Brand> brandList = bikeService.takeAllBrand();
			request.setAttribute(RequestParameter.BRAND_LIST.parameter(), brandList);

			List<BikeType> bikeTypeList = bikeService.takeAllBikeType();
			request.setAttribute(RequestParameter.BIKE_TYPE_LIST.parameter(), bikeTypeList);

			router.setPagePath(PageConstant.ADD_BIKE_PAGE);

		} catch (ServiceException e) {
			if (CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
				logger.log(Level.ERROR, "Add bike exception, " + ConvertPrintStackTraceToString.convert(e));
				router.setPagePath(PageConstant.ERROR_PAGE);
			} else {
				request.setAttribute(RequestParameter.ERROR.parameter(),
						CommandExceptionHandler.takeLogicExceptionMessage(e));
				router.setPagePath(PageConstant.BIKE_TYPE_PAGE);
				RequestParameterHandler.addParamToRequest(request);
			}
		}

		return router;
	}

}
