package by.postnikov.rentbike.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.dao.BikeDAO;
import by.postnikov.rentbike.dao.DAOFactory;
import by.postnikov.rentbike.dao.ParkingDAO;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.BikeProductState;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.BikeService;
import by.postnikov.rentbike.service.CloneRentBikeObject;
import by.postnikov.rentbike.validator.BikeParameterValidator;
import by.postnikov.rentbike.validator.UserParameterValidator;

public class BikeServiceImpl implements BikeService {

	private static Logger logger = LogManager.getLogger();

	private final static int MAX_SPEED_COUNT = 99;
	private final static String ZERO_PARAM = "0";

	public BikeServiceImpl() {
	}

	@Override
	public String addBike(Map<String, String> requestParameters, Bike bike) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		Brand brand = new Brand();
		String brandIdString = requestParameters.get(RequestParameter.BRAND_ID.parameter());
		if (!UserParameterValidator.idValidate(brandIdString)) {
			logger.log(Level.ERROR, "brandId - " + brandIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		brand.setId(Long.parseLong(brandIdString));
		bike.setBrand(brand);

		String model = requestParameters.get(RequestParameter.MODEL.parameter());
		if (!BikeParameterValidator.modelValidate(model)) {
			logger.log(Level.ERROR, "model - " + model + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setModel(requestParameters.get(RequestParameter.MODEL.parameter()));

		String wheelSizeString = (requestParameters.get(RequestParameter.WHEEL_SIZE.parameter()));
		if (!BikeParameterValidator.wheelSizeValidate(wheelSizeString)) {
			logger.log(Level.ERROR, "wheelSize - " + wheelSizeString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setWheelSize(Integer.parseInt(wheelSizeString));

		String speedCountString = requestParameters.get(RequestParameter.SPEED_COUNT.parameter());
		if (!BikeParameterValidator.speedCountValidate(speedCountString)) {
			logger.log(Level.ERROR, "speedCount - " + speedCountString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setSpeedCount(Integer.parseInt(speedCountString));

		bike.setPicturePath(requestParameters.get(RequestParameter.PICTURE.parameter()));

		BikeType bikeType = new BikeType();
		String bikeTypeIdString = requestParameters.get(RequestParameter.BIKE_TYPE_ID.parameter());
		if (!UserParameterValidator.idValidate(bikeTypeIdString)) {
			logger.log(Level.ERROR, "bikeTypeId - " + bikeTypeIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		bikeType.setId(Long.parseLong(bikeTypeIdString));
		bike.setBikeType(bikeType);

		try {
			return bikeDao.addBike(bike);
		} catch (DAOException e) {
			throw new ServiceException("Add bike exception", e);
		}

	}

	@Override
	public String addBrand(Map<String, String> requestParameters, Brand brand) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		String brandName = requestParameters.get(RequestParameter.BRAND.parameter());
		if (!BikeParameterValidator.brandValidate(brandName)) {
			logger.log(Level.ERROR, "brandName - " + brandName + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		brand.setBrand(brandName);

		brand.setBrand(requestParameters.get(RequestParameter.BRAND.parameter()));
		try {
			return bikeDao.addBrand(brand);
		} catch (DAOException e) {
			throw new ServiceException("Add bike brand exception", e);
		}

	}

	@Override
	public String addBikeType(String bikeTypeString) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		if (!BikeParameterValidator.bikeTypeValidate(bikeTypeString)) {
			return MessagePage.VALIDATION_ERROR.message();
		}

		BikeType bikeType = new BikeType();
		bikeType.setBikeType(bikeTypeString);

		try {
			return bikeDao.addBikeType(bikeType);
		} catch (DAOException e) {
			throw new ServiceException("Add bike type exception", e);
		}
	}

	@Override
	public List<BikeType> takeAllBikeType() throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		try {
			return bikeDao.takeAllBikeType();
		} catch (DAOException e) {
			throw new ServiceException("Get bike type exception", e);
		}

	}

	@Override
	public List<Brand> takeAllBrand() throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		try {
			return bikeDao.takeAllBrand();
		} catch (DAOException e) {
			throw new ServiceException("Get bike type exception", e);
		}

	}

	@Override
	public String findBike(Map<String, String> requestParameters, List<Bike> bikeList, PageInfo pageInfo)
			throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDAO = daoFactory.getBikeDAO();

		String brandIdString = requestParameters.get(RequestParameter.BRAND_ID.parameter());
		if (!ZERO_PARAM.equals(brandIdString) && !UserParameterValidator.idValidate(brandIdString)) {
			logger.log(Level.ERROR, "brandId - " + brandIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long brandId = Long.parseLong(brandIdString);

		String bikeTypeIdString = requestParameters.get(RequestParameter.BIKE_TYPE_ID.parameter());
		if (!ZERO_PARAM.equals(bikeTypeIdString) && !UserParameterValidator.idValidate(bikeTypeIdString)) {
			logger.log(Level.ERROR, "bikeTypeId - " + bikeTypeIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long bikeTypeId = Long.parseLong(bikeTypeIdString);

		String model = requestParameters.get(RequestParameter.MODEL.parameter());
		if (!model.isEmpty() && !BikeParameterValidator.modelValidate(model)) {
			logger.log(Level.ERROR, "model - " + model + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}

		int minSpeedCount = 0;
		String minSpeedCountString = requestParameters.get(RequestParameter.MIN_SPEED_COUNT.parameter());
		if (!minSpeedCountString.isEmpty() && !ZERO_PARAM.equals(minSpeedCountString)) {
			if (!BikeParameterValidator.speedCountValidate(minSpeedCountString)) {
				logger.log(Level.ERROR, "minSpeedCountString - " + minSpeedCountString + " is wrong");
				return MessagePage.VALIDATION_ERROR.message();
			}
			minSpeedCount = Integer.parseInt(minSpeedCountString);
		}

		int maxSpeedCount = 0;
		String maxSpeedCountString = requestParameters.get(RequestParameter.MAX_SPEED_COUNT.parameter());
		if (!maxSpeedCountString.isEmpty() && !ZERO_PARAM.equals(maxSpeedCountString)) {
			if (!BikeParameterValidator.speedCountValidate(maxSpeedCountString)) {
				logger.log(Level.ERROR, "maxSpeedCountString - " + maxSpeedCountString + " is wrong");
				return MessagePage.VALIDATION_ERROR.message();
			}
			maxSpeedCount = Integer.parseInt(maxSpeedCountString);
		}

		if (maxSpeedCount < minSpeedCount) {
			maxSpeedCount = MAX_SPEED_COUNT;
		}

		try {
			bikeDAO.findBike(bikeList, brandId, bikeTypeId, model, minSpeedCount, maxSpeedCount, pageInfo);
		} catch (DAOException e) {
			throw new ServiceException("Find bike exception", e);
		}

		return "";

	}

	@Override
	public String takeBikeByID(String bikeIdString, Bike bike) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDAO = daoFactory.getBikeDAO();

		if (!UserParameterValidator.idValidate(bikeIdString)) {
			logger.log(Level.ERROR, "bikeIdString - " + bikeIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long bikeId = Long.parseLong(bikeIdString);

		try {
			bikeDAO.takeBikeById(bikeId, bike);
		} catch (DAOException e) {
			throw new ServiceException("Get bike by id error", e);
		}

		return "";
	}

	@Override
	public String addBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList)
			throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDAO = daoFactory.getBikeDAO();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();

		CloneRentBikeObject<BikeProduct> cloneRentBikeObject = new CloneRentBikeObject<>();

		Parking parking = null;

		String countString = requestParameters.get(RequestParameter.BIKE_COUNT.parameter());
		if (!BikeParameterValidator.countValidate(countString)) {
			logger.log(Level.ERROR, "count - " + countString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		int bikeCount = Integer.parseInt(countString);

		String valueString = requestParameters.get(RequestParameter.BIKE_VALUE.parameter());
		if (!BikeParameterValidator.amountValidate(valueString)) {
			logger.log(Level.ERROR, "value - " + valueString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		BigDecimal bikeValue = new BigDecimal(valueString);

		String rentPriceString = requestParameters.get(RequestParameter.BIKE_RENT_PRICE.parameter());
		if (!BikeParameterValidator.amountValidate(rentPriceString)) {
			logger.log(Level.ERROR, "Rent ptice has not been verified");
			return null;
		}
		BigDecimal rentPrice = new BigDecimal(rentPriceString);

		String bikeIdString = requestParameters.get(RequestParameter.BIKE_ID.parameter());
		if (!UserParameterValidator.idValidate(bikeIdString)) {
			logger.log(Level.ERROR, "bikeIdString - " + bikeIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long bikeId = Long.parseLong(bikeIdString);

		String pakingIdString = requestParameters.get(RequestParameter.PARKING_ID.parameter());
		if (!UserParameterValidator.idValidate(pakingIdString)) {
			logger.log(Level.ERROR, "pakingIdString - " + pakingIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long parkingId = Long.parseLong(pakingIdString);

		try {
			Bike bike = new Bike();
			bikeDAO.takeBikeById(bikeId, bike);
			parking = parkingDAO.findParkingById(parkingId); // TODO ???

			BikeProduct bikeProduct = new BikeProduct();

			bikeProduct.setBike(bike);
			bikeProduct.setParking(parking);
			bikeProduct.setValue(bikeValue);
			bikeProduct.setRentPrice(rentPrice);

			bikeProductList = new ArrayList<>();
			for (int i = 0; i < bikeCount; i++) {
				bikeProductList.add(cloneRentBikeObject.clone(bikeProduct));
			}

			bikeProductList = bikeDAO.addBikeProduct(bikeProductList);

		} catch (NumberFormatException | DAOException e) {
			throw new ServiceException("Get bike by id error", e);
		}

		return "";
	}

	@Override
	public String findBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList,
			PageInfo pageInfo) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDAO = daoFactory.getBikeDAO();

		String brandIdString = requestParameters.get(RequestParameter.BRAND_ID.parameter());
		if (!ZERO_PARAM.equals(brandIdString) && !UserParameterValidator.idValidate(brandIdString)) {
			logger.log(Level.ERROR, "brandId - " + brandIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long brandId = Long.parseLong(brandIdString);

		String bikeTypeIdString = requestParameters.get(RequestParameter.BIKE_TYPE_ID.parameter());
		if (!ZERO_PARAM.equals(bikeTypeIdString) && !UserParameterValidator.idValidate(bikeTypeIdString)) {
			logger.log(Level.ERROR, "bikeTypeId - " + bikeTypeIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long bikeTypeId = Long.parseLong(bikeTypeIdString);

		String model = requestParameters.get(RequestParameter.MODEL.parameter());
		if (!model.isEmpty() && !BikeParameterValidator.modelValidate(model)) {
			logger.log(Level.ERROR, "model - " + model + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}

		String pakingIdString = requestParameters.get(RequestParameter.PARKING_ID.parameter());
		if (!ZERO_PARAM.equals(pakingIdString) && !UserParameterValidator.idValidate(pakingIdString)) {
			logger.log(Level.ERROR, "pakingIdString - " + pakingIdString + " is wrong");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long parkingId = Long.parseLong(pakingIdString);

		try {
			bikeDAO.findBikeProduct(parkingId, brandId, bikeTypeId, model, BikeProductState.AVAILABLE, bikeProductList,
					pageInfo);
		} catch (DAOException e) {
			throw new ServiceException("Find bike product exception", e);
		}

		return "";
	}

	@Override
	public String takeBikeProductById(String bikeProductIdString, BikeProduct bikeProduct) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDAO = daoFactory.getBikeDAO();

		try {
			if (!UserParameterValidator.idValidate(bikeProductIdString)) {
				logger.log(Level.ERROR, "bikeProductId - " + bikeProductIdString + " is wrong");
				return MessagePage.VALIDATION_ERROR.message();
			}
			long bikeProductId = Long.parseLong(bikeProductIdString);

			bikeDAO.takeBikeProductById(bikeProductId, bikeProduct);
		} catch (DAOException e) {
			throw new ServiceException("Take bike product by id exception", e);
		}

		return "";
	}

	@Override
	public String updateBike(Map<String, String> requestParameters, Bike bike) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		BikeDAO bikeDao = daoFactory.getBikeDAO();

		bike.setId(Long.parseLong(requestParameters.get(RequestParameter.BIKE_ID.parameter())));

		// validation
		Brand brand = new Brand();
		brand.setId(Long.parseLong(requestParameters.get(RequestParameter.BRAND_ID.parameter())));
		bike.setBrand(brand);

		String model = requestParameters.get(RequestParameter.MODEL.parameter());
		if (!BikeParameterValidator.modelValidate(model)) {
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setModel(requestParameters.get(RequestParameter.MODEL.parameter()));

		String wheelSizeString = (requestParameters.get(RequestParameter.WHEEL_SIZE.parameter()));
		if (!BikeParameterValidator.wheelSizeValidate(wheelSizeString)) {
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setWheelSize(Integer.parseInt(wheelSizeString));

		String speedCountString = requestParameters.get(RequestParameter.SPEED_COUNT.parameter());
		if (!BikeParameterValidator.speedCountValidate(speedCountString)) {
			return MessagePage.VALIDATION_ERROR.message();
		}
		bike.setSpeedCount(Integer.parseInt(speedCountString));

		bike.setPicturePath(requestParameters.get(RequestParameter.PICTURE.parameter()));

		BikeType bikeType = new BikeType();
		bikeType.setId(Long.parseLong(requestParameters.get(RequestParameter.BIKE_TYPE_ID.parameter())));
		bike.setBikeType(bikeType);

		try {
			return bikeDao.updateBike(bike);
		} catch (DAOException e) {
			throw new ServiceException("Add bike exception", e);
		}
	}

}
