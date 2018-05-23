package by.postnikov.rentbike.service;

import java.util.List;
import java.util.Map;

import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ServiceException;

public interface ParkingService {
	
	/**
	 * Validates incoming parameters and adds parking.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @return notice if validation failed or if parking with same address already exists.
	 * @throws ServiceException if any exception occurred.
	 */
	String addParking(Map<String, String> requestParameters) throws ServiceException;
	
	/**
	 * Validates incoming parameters and updates parking.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @return notice if validation failed or if parking with same address already exists.
	 * @throws ServiceException if any exception occurred.
	 */
	String updateParking(Map<String, String> requestParameters) throws ServiceException;
	
	/**
	 * Returns all parking lots.
	 * 
	 * @return list with all parking lots
	 * @throws ServiceException if any exception occurred.
	 */
	List<Parking> takeAllParking() throws ServiceException;
	
	/**
	 * Searches parking by id, and fills received parking object if found it.
	 * 
	 * @param pakingId as String.
	 * @param parking - empty parking object.
	 * @return notice if validation failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String findParkingById(String pakingId, Parking parking) throws ServiceException;

}
