package by.postnikov.rentbike.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.dao.DAOFactory;
import by.postnikov.rentbike.dao.ParkingDAO;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.ParkingService;
import by.postnikov.rentbike.validator.ParkingParameterValidator;
import by.postnikov.rentbike.validator.UserParameterValidator;

public class ParkingServiceImpl implements ParkingService {
	
	private static Logger logger = LogManager.getLogger();

	@Override
	public List<Parking> takeAllParking() throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO(); 

		try {
			return parkingDAO.takeAllParking();
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while take all parkings", e);
		}
	}

	@Override
	public String addParking(Map<String, String> requestParameters) throws ServiceException {

		Parking parking = new Parking();
		
		String address = requestParameters.get(RequestParameter.ADDRESS.parameter());
		if(!ParkingParameterValidator.addressValidate(address)) {
			return MessagePage.VALIDATION_ERROR.message();
		}
		parking.setAddress(address);
		
		String capacityString = requestParameters.get(RequestParameter.CAPACITY.parameter());
		if(!ParkingParameterValidator.capacityValidate(capacityString)) {
			return MessagePage.VALIDATION_ERROR.message();
		}	
		parking.setCapacity(Integer.parseInt(capacityString));

		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();

		try {
			return parkingDAO.addParking(parking);
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while adding the parking", e);
		}

	}

	@Override
	public String findParkingById(String pakingIdString, Parking parking) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();
		
		if(!UserParameterValidator.idValidate(pakingIdString)) {
			logger.log(Level.ERROR, "pakingIdString - " + pakingIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long parkingId = Long.parseLong(pakingIdString);
		
		try {
			Parking parkingTemp = parkingDAO.findParkingById(parkingId);
			parking.setId(parkingTemp.getId());
			parking.setAddress(parkingTemp.getAddress());
			parking.setCapacity(parkingTemp.getCapacity());

			return "";
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while find the parking by id", e);
		}
	}

}