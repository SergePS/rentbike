package by.postnikov.rentbike.dao;

import java.util.List;

import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;

public interface ParkingDAO {
	
	/**
	 * Adds parking to DB.
	 * 
	 * @param parking
	 * @return notice if parking with same address already exists
	 * @throws DAOException if occurred SQL exception
	 */
	String addParking(Parking parking) throws DAOException;
	
	/**
	 * Searches parking by id.
	 * 
	 * @param parkingId
	 * @return Parking if it was found or null if isn't
	 * @throws DAOException if occurred SQL exception
	 */
	Parking findParkingById(long parkingId) throws DAOException;
	
	/**
	 * Returns all parking lots.
	 * 
	 * @return List<Parking> parkingList with objects of the parking if they was found or empty list if not 
	 * @throws DAOException if occurred SQL exception
	 */
	List<Parking> takeAllParking() throws DAOException;

}
