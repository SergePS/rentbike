package by.postnikov.rentbike.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import by.postnikov.rentbike.command.ApplicationProperty;
import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.command.RequestParameter;
import by.postnikov.rentbike.dao.DAOFactory;
import by.postnikov.rentbike.dao.DateFormatting;
import by.postnikov.rentbike.dao.ParkingDAO;
import by.postnikov.rentbike.dao.UserDAO;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.entity.UserOrder;
import by.postnikov.rentbike.exception.DAOException;
import by.postnikov.rentbike.exception.ServiceException;
import by.postnikov.rentbike.service.DateFormatter;
import by.postnikov.rentbike.service.UserService;
import by.postnikov.rentbike.validator.UserParameterValidator;

public class UserServiceImpl implements UserService {

	private static Logger logger = LogManager.getLogger();

	private static final long EMPTY_Id = 0;

	private final static String FREE_RENT_TIME_PROP_KEY = "free_rent_time";
	private final static int FREE_RENT_TIME_PERIOD_PER_MINUTE = Integer
			.parseInt(ApplicationProperty.takeProperty().getProperty(FREE_RENT_TIME_PROP_KEY));

	public UserServiceImpl() {
	}

	@Override
	public String login(Map<String, String> requestParameters, char[] password, User user) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		String login = requestParameters.get(RequestParameter.LOGIN.parameter());
		if (!UserParameterValidator.loginValidate(login)) {
			logger.log(Level.DEBUG, "login validatation error, login = " + login);
			return MessagePage.VALIDATION_ERROR.message();
		}

		if (!UserParameterValidator.passwordValidate(password)) {
			logger.log(Level.DEBUG, "password validatation error");
			return MessagePage.VALIDATION_ERROR.message();
		}

		user.setLogin(login);

		try {
			userDAO.login(user, password);
		} catch (DAOException e) {
			throw new ServiceException("An exeption occured in the layer Service while getting user data", e);
		}

		return "";
	}

	@Override
	public String register(Map<String, String> requestParameters, char[] password, User user) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		String login = requestParameters.get(RequestParameter.LOGIN.parameter());
		if (!UserParameterValidator.loginValidate(login)) {
			logger.log(Level.DEBUG, "login validatation error, login = " + login);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setLogin(login);

		if (!UserParameterValidator.passwordValidate(password)) {
			logger.log(Level.DEBUG, "password validatation error");
			return MessagePage.VALIDATION_ERROR.message();
		}

		String name = requestParameters.get(RequestParameter.NAME.parameter());
		if (!UserParameterValidator.nameValidate(name)) {
			logger.log(Level.DEBUG, "name validatation error, name = " + name);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setName(name);

		String surname = requestParameters.get(RequestParameter.SURNAME.parameter());
		if (!UserParameterValidator.nameValidate(surname)) {
			logger.log(Level.DEBUG, "surname validatation error, surname = " + surname);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setSurname(surname);

		String email = requestParameters.get(RequestParameter.EMAIL.parameter());
		if (!UserParameterValidator.emailValidate(email)) {
			logger.log(Level.DEBUG, "email validatation error, email = " + email);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setEmail(email);

		String birthday = requestParameters.get(RequestParameter.BIRTHDAY.parameter());
		if (!UserParameterValidator.birthdayValidate(birthday)) {
			logger.log(Level.DEBUG, "birthday validatation error, birthday = " + birthday);
			return MessagePage.USER_IS_TOO_YOUNG.message();
		}
		user.setBirthday(DateFormatting.modifyDateToDB(birthday));

		user.setRegistrationDate(DateFormatting.getCurrentDate());

		String creditCard = requestParameters.get(RequestParameter.CRADIT_CARD.parameter());
		if (!UserParameterValidator.craditcardValidate(creditCard)) {
			logger.log(Level.DEBUG, "creditCard validatation error, creditCard = " + creditCard);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setCreditCard(creditCard);

		try {
			return userDAO.register(user, password);
		} catch (DAOException e) {
			throw new ServiceException("Add user to DB error", e);
		}

	}

	@Override
	public BikeOrder createOrder(User user, BikeProduct bikeProduct) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		BikeOrder bikeOrder = new BikeOrder();
		bikeOrder.setUser(user);
		bikeOrder.setBikeProductId(bikeProduct.getId());
		bikeOrder.setBike(bikeProduct.getBike());
		bikeOrder.setStartParking(bikeProduct.getParking());
		bikeOrder.setStartTime(DateFormatter.takeCurrentDateTimeToDB());
		bikeOrder.setBikeValue(bikeProduct.getValue());
		bikeOrder.setRentPrice(bikeProduct.getRentPrice());

		try {
			userDAO.createOrder(bikeOrder);
		} catch (DAOException e) {
			throw new ServiceException(e);
		}

		return bikeOrder;
	}

	@Override
	public BikeOrder findOpenOrder(User user) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		try {
			return userDAO.findOpenOrder(user);
		} catch (DAOException e) {
			throw new ServiceException("Find open order error", e);
		}

	}

	@Override
	public String findOpenOrderById(String orderIdString, BikeOrder bikeOrder) throws ServiceException {
		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		if (!UserParameterValidator.idValidate(orderIdString)) {
			logger.log(Level.DEBUG, "Id validatation error, id = " + orderIdString);
			return MessagePage.VALIDATION_ERROR.message();
		}
		long orderId = Long.parseLong(orderIdString);

		try {
			userDAO.findOpenOrderById(orderId, bikeOrder);
		} catch (DAOException e) {
			throw new ServiceException("Find open order by id error", e);
		}
		
		return "";
	}

	@Override
	public String closeOrder(Map<String, String> requestParameters) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();
		ParkingDAO parkingDAO = daoFactory.getParkingDAO();

		// orderId validation
		String orderIdString = requestParameters.get(RequestParameter.ORDER_ID.parameter());
		if (!UserParameterValidator.idValidate(orderIdString)) {
			logger.log(Level.DEBUG, "orderId validatation error");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long orderId = Long.parseLong(orderIdString);

		try {
			BikeOrder bikeOrder = new BikeOrder();
			userDAO.findOpenOrderById(orderId, bikeOrder);
			
			if (bikeOrder.getId() == 0) {
				logger.log(Level.DEBUG, "order not exist");
				return MessagePage.ORDER_NOT_EXIST.message();
			}
		} catch (DAOException e) {
			throw new ServiceException("Find open order by id error", e);
		}

		// parkingId validation
		String finishParkingIdString = requestParameters.get(RequestParameter.FINISH_PARKING_ID.parameter());
		if (!UserParameterValidator.idValidate(finishParkingIdString)) {
			logger.log(Level.DEBUG, "parkingId validatation error");
			return MessagePage.VALIDATION_ERROR.message();
		}
		long finishParkingId = Long.parseLong(finishParkingIdString);

		try {
			Parking parking = parkingDAO.findParkingById(finishParkingId);
			if (parking == null) {
				logger.log(Level.DEBUG, "parking validation error: parking not exist");
				return MessagePage.VALIDATION_ERROR.message();
			}
		} catch (DAOException e) {
			throw new ServiceException("Find parking by id error", e);
		}

		// finish time validation
		String startTime = requestParameters.get(RequestParameter.START_TIME.parameter());
		String finishTime = DateFormatter.takeCurrentDateTimeToDB();

		long rentMinutes = DateFormatter.takeMinutesBetweenDates(startTime, finishTime);

		if (rentMinutes < 0) {
			logger.log(Level.DEBUG, "finish time validatation error");
			return MessagePage.VALIDATION_ERROR.message();
		}

		// calculate payment end close order
		BigDecimal rentPrice = new BigDecimal(requestParameters.get(RequestParameter.RENT_PRICE.parameter()));
		BigDecimal payment;

		if (rentMinutes > FREE_RENT_TIME_PERIOD_PER_MINUTE) {
			payment = rentPrice.multiply(new BigDecimal(rentMinutes));
		} else {
			payment = BigDecimal.ZERO;
		}

		try {
			userDAO.closeOrder(orderId, finishParkingId, finishTime, payment);
		} catch (DAOException e) {
			throw new ServiceException("Close order error", e);
		}

		return BigDecimal.ZERO.equals(payment) ? "" : payment.toString();

	}

	@Override
	public List<UserOrder> takeAllUsers() throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		try {
			return userDAO.takeAllUsers();
		} catch (DAOException e) {
			throw new ServiceException("take all user error", e);
		}
	}

	@Override
	public String userUpdate(Map<String, String> requestParameters, User user) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		String login = requestParameters.get(RequestParameter.LOGIN.parameter());
		if (!UserParameterValidator.loginValidate(login)) {
			logger.log(Level.DEBUG, "login validatation error, login = " + login);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setLogin(login);

		String name = requestParameters.get(RequestParameter.NAME.parameter());
		if (!UserParameterValidator.nameValidate(name)) {
			logger.log(Level.DEBUG, "name validatation error, name = " + name);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setName(name);

		String surname = requestParameters.get(RequestParameter.SURNAME.parameter());
		if (!UserParameterValidator.nameValidate(surname)) {
			logger.log(Level.DEBUG, "surname validatation error, surname = " + surname);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setSurname(surname);

		String email = requestParameters.get(RequestParameter.EMAIL.parameter());
		if (!UserParameterValidator.emailValidate(email)) {
			logger.log(Level.DEBUG, "email validatation error, email = " + email);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setEmail(email);

		String birthday = requestParameters.get(RequestParameter.BIRTHDAY.parameter());
		if (!UserParameterValidator.birthdayValidate(birthday)) {
			logger.log(Level.DEBUG, "birthday validatation error, birthday = " + birthday);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setBirthday(birthday);

		String creditCard = requestParameters.get(RequestParameter.CRADIT_CARD.parameter());
		if (!UserParameterValidator.craditcardValidate(creditCard)) {
			logger.log(Level.DEBUG, "creditCard validatation error, creditCard = " + creditCard);
			return MessagePage.VALIDATION_ERROR.message();
		}
		user.setCreditCard(creditCard);

		try {
			return userDAO.updateUser(user);
		} catch (DAOException e) {
			throw new ServiceException("Update user to DB error", e);
		}

	}

	@Override
	public String updatePassword(char[] currentPassword, char[] password, User user) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		if (!UserParameterValidator.passwordValidate(password)) {
			return MessagePage.VALIDATION_ERROR.message();
		}

		user.setId(EMPTY_Id);
		try {
			userDAO.login(user, currentPassword);

			if (user.getId() == EMPTY_Id) {
				logger.log(Level.DEBUG, "current password wrong for user - " + user.getLogin() + "passw - "
						+ String.valueOf(currentPassword));
				return MessagePage.CURRENT_PASSW_WRONG.message();
			}

			if (!UserParameterValidator.passwordValidate(password)) {
				return MessagePage.VALIDATION_ERROR.message();
			}

			userDAO.updatePassword(password, user);

		} catch (DAOException e) {
			throw new ServiceException("An exeption occured in the layer Service while update password of user", e);
		}

		return "";
	}

	@Override
	public List<BikeOrder> findAllOrderByUser(User user) throws ServiceException {

		DAOFactory daoFactory = DAOFactory.getInstance();
		UserDAO userDAO = daoFactory.getUserDAO();

		try {
			List<BikeOrder>  bikeOrderList = userDAO.findAllOrderByUser(user.getId());
			for (BikeOrder bikeOrder : bikeOrderList) {
				bikeOrder.setStartTime(DateFormatter.modifyDateTimeToView(bikeOrder.getStartTime()));
				if (bikeOrder.getFinishTime() != null) {
					bikeOrder.setFinishTime(DateFormatter.modifyDateTimeToView(bikeOrder.getFinishTime()));
				}
			}
			return bikeOrderList;

		} catch (DAOException e) {
			throw new ServiceException("An exception was thrown when searching for all users", e);
		}
	}
	
}
