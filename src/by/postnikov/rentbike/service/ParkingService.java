package by.postnikov.rentbike.service;

import java.util.List;
import java.util.Map;

import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.ServiceException;

public interface ParkingService {
	
	String addParking(Map<String, String> requestParameters) throws ServiceException;
	
	List<Parking> takeAllParking() throws ServiceException;
	
	String findParkingById(String pakingId, Parking parking) throws ServiceException;

}
