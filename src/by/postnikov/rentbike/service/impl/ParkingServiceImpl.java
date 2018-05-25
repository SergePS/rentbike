package by.postnikov.rentbike.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.dao.DAOFactory;
import by.postnikov.rentbike.dao.ParkingDAO;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;
import by.postnikov.rentbike.exception.ExceptionMessage;
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
	public void addParking(Map<String, String> requestParameters) throws ServiceException {

		Parking parking = new Parking();
		
		String address = requestParameters.get(RequestParameter.ADDRESS.parameter());
		if(!ParkingParameterValidator.addressValidate(address)) {
			throw new  ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}
		parking.setAddress(address);
		
		String capacityString = requestParameters.get(RequestParameter.CAPACITY.parameter());
		if(!ParkingParameterValidator.capacityValidate(capacityString)) {
			throw new  ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}	
		parking.setCapacity(Integer.parseInt(capacityString));

		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();

		try {
			parkingDAO.addParking(parking);
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while adding the parking", e);
		}

	}
	
	@Override
	public void updateParking(Map<String, String> requestParameters) throws ServiceException {
		
		Parking parking = new Parking();
		
		String parkingIdString = requestParameters.get(RequestParameter.PARKING_ID.parameter());
		if(!UserParameterValidator.idValidate(parkingIdString)) {
			logger.log(Level.ERROR, "pakingIdString - " + parkingIdString + " is wrong");
			throw new  ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}
		long parkingId = Long.parseLong(parkingIdString);
		parking.setId(parkingId);
		
		
		String address = requestParameters.get(RequestParameter.ADDRESS.parameter());
		if(!ParkingParameterValidator.addressValidate(address)) {
			throw new  ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}
		parking.setAddress(address);
		
		String capacityString = requestParameters.get(RequestParameter.CAPACITY.parameter());
		if(!ParkingParameterValidator.capacityValidate(capacityString)) {
			throw new  ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}	
		parking.setCapacity(Integer.parseInt(capacityString));

		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();

		try {
			parkingDAO.updateParking(parking);
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while updating the parking", e);
		}
	}

	@Override
	public Parking findParkingById(String pakingIdString) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();
		
		if(!UserParameterValidator.idValidate(pakingIdString)) {
			throw new ServiceException(ExceptionMessage.VALIDATION_ERROR.toString());
		}
		long parkingId = Long.parseLong(pakingIdString);
		
		try {
			return parkingDAO.findParkingById(parkingId);
		} catch (DAOException e) {
			throw new ServiceException("An error occurred while find the parking by id", e);
		}
	}

}
