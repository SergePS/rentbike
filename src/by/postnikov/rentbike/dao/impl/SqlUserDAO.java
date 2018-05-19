package by.postnikov.rentbike.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import by.postnikov.rentbike.command.MessagePage;
import by.postnikov.rentbike.connection.ConnectionPool;
import by.postnikov.rentbike.connection.WrapperConnection;
import by.postnikov.rentbike.dao.DateFormatting;
import by.postnikov.rentbike.dao.UserDAO;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.entity.UserOrder;
import by.postnikov.rentbike.entity.UserRole;
import by.postnikov.rentbike.entity.UserState;
import by.postnikov.rentbike.exception.DAOException;

public class SqlUserDAO implements UserDAO {

	private static Logger logger = LogManager.getLogger();

	private final static String FIND_USER_BY_LOGIN_PASSW = "SELECT id, login, name, surname, email, birthday, registrationDate, role, state, creditCard FROM users WHERE login = ? and password = MD5(?)";

	private final static String CREATE_USER_CU = "INSERT INTO users (login, password, name, surname, email, birthday, registrationDate, creditCard)  VALUES (?, MD5(?), ?, ?, ?, ?, ?, ?)";
	private final static int CU_LOGIN = 1; // CU - Create User
	private final static int CU_PASSWORD = 2;
	private final static int CU_NAME = 3;
	private final static int CU_SURNAME = 4;
	private final static int CU_EMAIL = 5;
	private final static int CU_BIRTHDAY = 6;
	private final static int CU_REGISTRATION_DATE = 7;
	private final static int CU_CREDIT_CARD = 8;

	private final static String CREATE_ORDER_CO = "INSERT INTO orders (userId, bikeProductId, startParkingId, startTime, bikeValue, rentPrice) VALUES (?, ?, ?, ?, ?, ?)";
	private final static int CO_USER_ID = 1; // CO - Create Order
	private final static int CO_BIKE_PRODUCT_ID = 2;
	private final static int CO_START_PARKING_ID = 3;
	private final static int CO_START_TIME = 4;
	private final static int CO_BIKE_VALUE = 5;
	private final static int CO_RENT_PRICE = 6;

	private final static String FIND_OPEN_ORDER_FOO = "SELECT o.id, o.bikeProductId, bk.id AS bikeId, bk.brandId, br.brand, bk.model, bk.wheelSize, bk.speedCount, bk.picture, bk.bikeTypeId, bt.type, pk.id AS parkingId, pk.address, o.startTime, o.bikeValue, o.rentPrice FROM orders o LEFT JOIN bikes bk ON (SELECT bikeId FROM bikeproduct WHERE bikeproduct.id=o.bikeProductId)=bk.id LEFT JOIN brands br ON bk.brandId = br.id LEFT JOIN biketype bt ON bk.bikeTypeId = bt.id LEFT JOIN parkings pk ON o.startParkingId=pk.id WHERE userId = ? AND finishTime IS NULL";
	private final static int FOO_USER_ID = 1;

	private final static String FIND_OPEN_ORDER_BY_ID_FOOBI = "SELECT o.id, o.bikeProductId, o.userId, bk.id AS bikeId, bk.brandId, br.brand, bk.model, bk.wheelSize, bk.speedCount, bk.picture, bk.bikeTypeId, bt.type, pk.id AS parkingId, pk.address, o.startTime, o.bikeValue, o.rentPrice FROM orders o LEFT JOIN bikes bk ON (SELECT bikeId FROM bikeproduct WHERE bikeproduct.id=o.bikeProductId)=bk.id LEFT JOIN brands br ON bk.brandId = br.id LEFT JOIN biketype bt ON bk.bikeTypeId = bt.id LEFT JOIN parkings pk ON o.startParkingId=pk.id WHERE o.id = ? AND finishTime IS NULL";
	private final static int FOOBI_ORDER_ID = 1; // FOOBI = Find Open Order By Id

	private final static String ID = "id";
	private final static String BIKE_PRODUCT_ID = "bikeProductId";
	private final static String USER_ID = "userId";
	private final static String BIKE_ID = "bikeId";
	private final static String BRAND_ID = "brandId";
	private final static String BRAND = "brand";
	private final static String MODEL = "model";
	private final static String WHEELSIZE = "wheelSize";
	private final static String SPEEDCOUNT = "speedCount";
	private final static String PICTURE = "picture";
	private final static String BIKETYPE_ID = "bikeTypeId";
	private final static String BIKE_TYPE = "type";
	private final static String PARKING_ID = "parkingId";
	private final static String ADDRESS = "address";
	private final static String BIKE_VALUE = "bikeValue";
	private final static String RENT_PRICE = "rentPrice";
	private final static String START_TIME = "startTime";

	private final static String CLOSE_ORDER_CLO = "UPDATE orders SET finishParkingId = ?, finishTime = ?, payment = ? WHERE id = ?";
	private final static int CLO_FINISH_PARKING = 1; // CLO - CLose Order
	private final static int CLO_FINISH_TIME = 2;
	private final static int CLO_PAYMENT = 3;
	private final static int CLO_ID = 4;

	private final static String CHANGE_BIKE_STATE_TO_RESERVED_CBSTR = "UPDATE bikeproduct SET state = 'reserved' WHERE id = (SELECT bikeProductId FROM orders WHERE orders.id = ?)";
	private final static int CBSTR_BIKE_ORDER_ID = 1; // CBSTR - Change Bike State To Reserved

	private final static String CHANGE_BIKE_STATE_TO_AVAILABLE_CBSTA = "UPDATE bikeproduct SET state = 'available', parkingId = ? WHERE id = (SELECT bikeProductId FROM orders WHERE orders.id = ?)";
	private final static int CBSTA_PARKING_ID = 1; // CBSTA - Change Bike State To Available
	private final static int CBSTA_BIKE_ORDER_ID = 2;

	private final static String SELECT_ALL_USERS = "SELECT u.id, u.login, u.name, u.surname, u.email, u.birthday, u.registrationDate, u.role, u.state, u.creditCard, o.id AS orderId FROM users u LEFT JOIN (SELECT id, userId FROM orders WHERE finishTime IS NULL) o ON u.id= o.userId";
	private final static String LOGIN = "login";
	private final static String NAME = "name";
	private final static String SURNAME = "surname";
	private final static String EMAIL = "email";
	private final static String BIRTHDAY = "birthday";
	private final static String REGISTRATION_DATE = "registrationDate";
	private final static String ROLE = "role";
	private final static String STATE = "state";
	private final static String CREDIT_CARD = "creditCard";
	private final static String ORDER_ID = "orderId";

	private final static String UPDATE_USER_UU = "UPDATE users SET login = ?, name = ?, surname = ?, email = ?, birthday = ?, creditCard = ? WHERE id = ?";
	private final static int UU_LOGIN = 1; // UU - Update User
	private final static int UU_NAME = 2;
	private final static int UU_SURNAME = 3;
	private final static int UU_EMAIL = 4;
	private final static int UU_BIRTHDAY = 5;
	private final static int UU_CREDIT_CARD = 6;
	private final static int UU_ID = 7;

	private final static String UPDATE_PASSWORD_UP = "UPDATE users SET password = MD5(?) where id = ?";
	private final static int UP_PASSWORD = 1; // UP - Update Password
	private final static int UP_ID = 2;
	
	private final static String TAKE_ALL_USER_ORDERS_TAUO = "SELECT o.id, br.brand, bk.model, o.startTime, sp.address as startParking, o.finishTime, fp.address as finishParking, o.rentPrice, TIMESTAMPDIFF(MINUTE, startTime, finishTime) as minute, payment FROM orders o LEFT JOIN parkings sp ON o.startParkingId = sp.id LEFT JOIN parkings fp ON o.finishParkingId = fp.id LEFT JOIN bikeproduct bp ON o.bikeProductId = bp.id LEFT JOIN bikes bk ON bp.bikeId = bk.id LEFT JOIN brands br ON bk.brandId = br.id WHERE userId = ? ORDER by ID desc";
	private final static int TAUO_USER_ID = 1;
	private final static String FINISH_TIME = "finishTime";
	private final static String PAYMENT = "payment";
	private final static String START_PARKING = "startParking";
	private final static String FINISH_PARKING = "finishParking";
	
	
	
	@Override
	public String register(User user, char[] password) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(CREATE_USER_CU);

		try {
			preparedStatement.setString(CU_LOGIN, user.getLogin());
			preparedStatement.setString(CU_PASSWORD, String.valueOf(password));
			preparedStatement.setString(CU_NAME, user.getName());
			preparedStatement.setString(CU_SURNAME, user.getSurname());
			preparedStatement.setString(CU_EMAIL, user.getEmail());
			preparedStatement.setString(CU_BIRTHDAY, user.getBirthday());
			preparedStatement.setString(CU_REGISTRATION_DATE, user.getRegistrationDate());
			preparedStatement.setString(CU_CREDIT_CARD, user.getCreditCard());

			preparedStatement.executeUpdate();

		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.log(Level.ERROR, "user with such login or e-mail already exists", e);
			return MessagePage.USER_DUBLICATE_ERROR.message();
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while user adding to the DB", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return "";

	}

	@Override
	public void login(User user, char[] password) throws DAOException{

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(FIND_USER_BY_LOGIN_PASSW);

		try {
			preparedStatement.setString(CU_LOGIN, user.getLogin());
			preparedStatement.setString(CU_PASSWORD, String.valueOf(password));

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				user.setId(resultSet.getLong(ID));
				user.setLogin(resultSet.getString(LOGIN));
				user.setName(resultSet.getString(NAME));
				user.setSurname(resultSet.getString(SURNAME));
				user.setEmail(resultSet.getString(EMAIL));
				user.setBirthday(DateFormatting.modifyDateToView(resultSet.getString(BIRTHDAY)));
				user.setRegistrationDate(DateFormatting.modifyDateToView(resultSet.getString(REGISTRATION_DATE)));
				user.setRole(UserRole.valueOf(resultSet.getString(ROLE).toUpperCase()));
				user.setState(UserState.valueOf(resultSet.getString(STATE).toUpperCase()));
				user.setCreditCard(resultSet.getString(CREDIT_CARD));
			}
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while reading user data from DB", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public String updateUser(User user) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(UPDATE_USER_UU);

		try {
			preparedStatement.setString(UU_LOGIN, user.getLogin());
			preparedStatement.setString(UU_NAME, user.getName());
			preparedStatement.setString(UU_SURNAME, user.getSurname());
			preparedStatement.setString(UU_EMAIL, user.getEmail());
			preparedStatement.setString(UU_BIRTHDAY, DateFormatting.modifyDateToDB(user.getBirthday()));
			preparedStatement.setString(UU_CREDIT_CARD, user.getCreditCard());
			preparedStatement.setLong(UU_ID, user.getId());

			preparedStatement.executeUpdate();

		} catch (MySQLIntegrityConstraintViolationException e) {
			logger.log(Level.ERROR, "user with such login or e-mail already exists" + e);
			return MessagePage.USER_DUBLICATE_ERROR.message();
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while updating the user", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return "";
	}

	@Override
	public BikeOrder createOrder(BikeOrder order) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(CREATE_ORDER_CO);

		try {
			wrapperConnection.setAutoCommit(false);
			preparedStatement.setLong(CO_USER_ID, order.getUser().getId());
			preparedStatement.setLong(CO_BIKE_PRODUCT_ID, order.getBikeProductId());
			preparedStatement.setLong(CO_START_PARKING_ID, order.getStartParking().getId());
			preparedStatement.setString(CO_START_TIME, order.getStartTime());
			preparedStatement.setBigDecimal(CO_BIKE_VALUE, order.getBikeValue());
			preparedStatement.setBigDecimal(CO_RENT_PRICE, order.getRentPrice());
			preparedStatement.executeUpdate();

			long orderId = ((com.mysql.jdbc.PreparedStatement) preparedStatement).getLastInsertID();
			order.setId(orderId);

			wrapperConnection.closeStatement(preparedStatement);

			preparedStatement = wrapperConnection.getPreparedStatement(CHANGE_BIKE_STATE_TO_RESERVED_CBSTR);
			preparedStatement.setLong(CBSTR_BIKE_ORDER_ID, orderId);
			preparedStatement.executeUpdate();

			wrapperConnection.commit();

		} catch (SQLException e) {
			wrapperConnection.rollback();
			throw new DAOException("An exeption occured in the layer DAO while adding the order to the DB", e);
		} finally {
			wrapperConnection.setAutoCommit(true);
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return order;
	}

	@Override
	public BikeOrder findOpenOrder(User user) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(FIND_OPEN_ORDER_FOO);

		BikeOrder bikeOrder = null;

		try {
			preparedStatement.setLong(FOO_USER_ID, user.getId());
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				bikeOrder = new BikeOrder();
				bikeOrder.setId(resultSet.getLong(ID));
				bikeOrder.setBikeProductId(resultSet.getLong(BIKE_PRODUCT_ID));

				Bike bike = new Bike();
				bike.setId(resultSet.getLong(BIKE_ID));
				Brand brand = new Brand();
				brand.setId(resultSet.getLong(BRAND_ID));
				brand.setBrand(resultSet.getString(BRAND));
				bike.setBrand(brand);
				bike.setModel(resultSet.getString(MODEL));
				bike.setWheelSize(resultSet.getInt(WHEELSIZE));
				bike.setSpeedCount(resultSet.getInt(SPEEDCOUNT));
				bike.setPicturePath(resultSet.getString(PICTURE));
				BikeType bikeType = new BikeType();
				bikeType.setId(resultSet.getLong(BIKETYPE_ID));
				bikeType.setBikeType(resultSet.getString(BIKE_TYPE));
				bike.setBikeType(bikeType);
				bikeOrder.setBike(bike);

				Parking parking = new Parking();
				parking.setId(resultSet.getLong(PARKING_ID));
				parking.setAddress(resultSet.getString(ADDRESS));
				bikeOrder.setStartParking(parking);

				bikeOrder.setBikeValue(resultSet.getBigDecimal(BIKE_VALUE));
				bikeOrder.setRentPrice(resultSet.getBigDecimal(RENT_PRICE));

				bikeOrder.setStartTime(resultSet.getString(START_TIME));

				bikeOrder.setUser(user);

			}
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while finding open order", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return bikeOrder;

	}

	@Override
	public void findOpenOrderById(long orderId, BikeOrder bikeOrder) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(FIND_OPEN_ORDER_BY_ID_FOOBI);

		try {
			preparedStatement.setLong(FOOBI_ORDER_ID, orderId);
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				bikeOrder.setId(orderId);
				bikeOrder.setBikeProductId(resultSet.getLong(BIKE_PRODUCT_ID));

				Bike bike = new Bike();
				bike.setId(resultSet.getLong(BIKE_ID));
				Brand brand = new Brand();
				brand.setId(resultSet.getLong(BRAND_ID));
				brand.setBrand(resultSet.getString(BRAND));
				bike.setBrand(brand);
				bike.setModel(resultSet.getString(MODEL));
				bike.setWheelSize(resultSet.getInt(WHEELSIZE));
				bike.setSpeedCount(resultSet.getInt(SPEEDCOUNT));
				bike.setPicturePath(resultSet.getString(PICTURE));
				BikeType bikeType = new BikeType();
				bikeType.setId(resultSet.getLong(BIKETYPE_ID));
				bikeType.setBikeType(resultSet.getString(BIKE_TYPE));
				bike.setBikeType(bikeType);
				bikeOrder.setBike(bike);

				Parking parking = new Parking();
				parking.setId(resultSet.getLong(PARKING_ID));
				parking.setAddress(resultSet.getString(ADDRESS));
				bikeOrder.setStartParking(parking);

				bikeOrder.setBikeValue(resultSet.getBigDecimal(BIKE_VALUE));
				bikeOrder.setRentPrice(resultSet.getBigDecimal(RENT_PRICE));

				bikeOrder.setStartTime(resultSet.getString(START_TIME));

				User user = new User();
				user.setId(resultSet.getLong(USER_ID));
			}

		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while finding open order", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public void closeOrder(long orderId, long finishParkingId, String finishTime, BigDecimal payment)
			throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(CLOSE_ORDER_CLO);

		try {
			wrapperConnection.setAutoCommit(false);
			preparedStatement.setLong(CLO_FINISH_PARKING, finishParkingId);
			preparedStatement.setString(CLO_FINISH_TIME, finishTime);
			preparedStatement.setBigDecimal(CLO_PAYMENT, payment);
			preparedStatement.setLong(CLO_ID, orderId);
			preparedStatement.executeUpdate();

			wrapperConnection.closeStatement(preparedStatement);

			preparedStatement = wrapperConnection.getPreparedStatement(CHANGE_BIKE_STATE_TO_AVAILABLE_CBSTA);
			preparedStatement.setLong(CBSTA_PARKING_ID, finishParkingId);
			preparedStatement.setLong(CBSTA_BIKE_ORDER_ID, orderId);
			preparedStatement.executeUpdate();

			wrapperConnection.commit();

		} catch (SQLException e) {
			wrapperConnection.rollback();
			throw new DAOException("An exeption occured in the layer DAO while closing order", e);
		} finally {
			wrapperConnection.setAutoCommit(true);
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public List<UserOrder> takeAllUsers() throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		Statement statement = wrapperConnection.getStatement();

		List<UserOrder> userOrderList = null;

		try {
			ResultSet resultSet = statement.executeQuery(SELECT_ALL_USERS);

			while (resultSet.next()) {
				if (userOrderList == null) {
					userOrderList = new ArrayList<>();
				}
				UserOrder userOrder = new UserOrder();
				User user = new User();
				user.setId(resultSet.getLong(ID));
				user.setLogin(resultSet.getString(LOGIN));
				user.setName(resultSet.getString(NAME));
				user.setSurname(resultSet.getString(SURNAME));
				user.setEmail(resultSet.getString(EMAIL));
				user.setBirthday(resultSet.getString(BIRTHDAY));
				user.setRegistrationDate(resultSet.getString(REGISTRATION_DATE));
				user.setRole(UserRole.valueOf(resultSet.getString(ROLE).toUpperCase()));
				user.setCreditCard(resultSet.getString(CREDIT_CARD));
				user.setState(UserState.valueOf(resultSet.getString(STATE).toUpperCase()));
				userOrder.setUser(user);

				BikeOrder bikeOrder = new BikeOrder();
				bikeOrder.setId(resultSet.getLong(ORDER_ID));
				userOrder.setBikeOrder(bikeOrder);

				userOrderList.add(userOrder);
			}

			return userOrderList;
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while getting all users", e);
		} finally {
			wrapperConnection.closeStatement(statement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public void updatePassword(char[] password, User user) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(UPDATE_PASSWORD_UP);

		try {
			preparedStatement.setString(UP_PASSWORD, String.valueOf(password));
			preparedStatement.setLong(UP_ID, user.getId());
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while updating password", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public List<BikeOrder> findAllOrderByUser(long userId) throws DAOException {
		
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(TAKE_ALL_USER_ORDERS_TAUO);
		
		List<BikeOrder> bikeOrderList = new ArrayList<>();
		
		try {
			preparedStatement.setLong(TAUO_USER_ID, userId);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			while (resultSet.next()) {
				BikeOrder bikeOrder = new BikeOrder();
				
				bikeOrder.setId(resultSet.getLong(ID));
				bikeOrder.setStartTime(resultSet.getString(START_TIME));
				bikeOrder.setFinishTime(resultSet.getString(FINISH_TIME));
				bikeOrder.setRentPrice(resultSet.getBigDecimal(RENT_PRICE));
				bikeOrder.setPayment(resultSet.getBigDecimal(PAYMENT));
				
				Bike bike = new Bike();
				Brand brand = new Brand();
				brand.setBrand(resultSet.getString(BRAND));
				bike.setBrand(brand);
				bike.setModel(resultSet.getString(MODEL));
				bikeOrder.setBike(bike);

				Parking startParking = new Parking();
				startParking.setAddress(resultSet.getString(START_PARKING));
				bikeOrder.setStartParking(startParking);

				Parking finishParking = new Parking();
				finishParking.setAddress(resultSet.getString(FINISH_PARKING));
				bikeOrder.setFinishParking(finishParking);
				
				bikeOrderList.add(bikeOrder);
			}
			
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while finding all orders", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
		
		return bikeOrderList;
	}

}
