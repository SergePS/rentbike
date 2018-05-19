package by.postnikov.rentbike.service;

import by.postnikov.rentbike.service.impl.BikeServiceImpl;
import by.postnikov.rentbike.service.impl.ParkingServiceImpl;
import by.postnikov.rentbike.service.impl.UserServiceImpl;

public class ServiceFactory {
	private static final ServiceFactory instance = new ServiceFactory();

	private static final UserService userService = new UserServiceImpl();
	private static final BikeService bikeService = new BikeServiceImpl();
	private static final ParkingService parkingService = new ParkingServiceImpl();

	private ServiceFactory() {
	}

	public UserService getUserService() {
		return userService;
	}

	public BikeService getBikeService() {
		return bikeService;
	}
	
	public ParkingService getParkingService() {
		return parkingService;
	}

	public static ServiceFactory getInstance() {
		return instance;
	}

}
