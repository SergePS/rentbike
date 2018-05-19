package by.postnikov.rentbike.dao;

import by.postnikov.rentbike.dao.impl.SqlBikeDAO;
import by.postnikov.rentbike.dao.impl.SqlParkingDAO;
import by.postnikov.rentbike.dao.impl.SqlUserDAO;


public class DAOFactory {
	
	private static final DAOFactory instance = new DAOFactory();
	
	private final UserDAO userDAO = new SqlUserDAO();
	private final BikeDAO bikeDAO = new SqlBikeDAO();
	private final ParkingDAO parkingDAO = new SqlParkingDAO();
	
	
	private DAOFactory() {}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public BikeDAO getBikeDAO() {
		return bikeDAO;
	}
	
	public ParkingDAO getParkingDAO() {
		return parkingDAO;
	}
		
	public static DAOFactory getInstance() {
		return instance;
	}

}
