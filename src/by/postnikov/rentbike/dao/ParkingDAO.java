package by.postnikov.rentbike.dao;

import java.util.List;

import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;

public interface ParkingDAO {
	
	String addParking(Parking parking) throws DAOException;
	
	Parking findParkingById(long parkingId) throws DAOException;
	
	List<Parking> takeAllParking() throws DAOException;

}
