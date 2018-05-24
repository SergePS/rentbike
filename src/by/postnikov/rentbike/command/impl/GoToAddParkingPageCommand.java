package by.postnikov.rentbike.command.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.Command;
import by.postnikov.rentbike.command.CommandExceptionHandler;
import by.postnikov.rentbike.command.PageConstant;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.controller.Router;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ConvertPrintStackTraceToString;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.service.ServiceFactory;

public class GoToAddParkingPageCommand implements Command {

	private static Logger logger = LogManager.getLogger();

	@Override
	public Router execute(HttpServletRequest request) {

		Router router = new Router();

		ServiceFactory serviceFactory = ServiceFactory.getInstance();
		ParkingService parkingService = serviceFactory.getParkingService();

		String parkingId = request.getParameter(RequestParameter.PARKING_ID.parameter());
		if (parkingId != null) {
			try {
				Parking parking = parkingService.findParkingById(parkingId);

				request.setAttribute(RequestParameter.PARKING.parameter(), parking);
			} catch (ServiceException e) {
				if (CommandExceptionHandler.takeLogicExceptionMessage(e).isEmpty()) {
					logger.log(Level.ERROR, "Take pakring by id error" + ConvertPrintStackTraceToString.convert(e));
					router.setPagePath(PageConstant.ERROR_PAGE);
				} else {
					router.setPagePath(PageConstant.ERROR_PAGE);
				}
			}
		}

		router.setPagePath(PageConstant.ADD_PARKING_PAGE);
		return router;
	}

}
