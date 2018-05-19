package by.postnikov.rentbike.dao;

import java.math.BigDecimal;
import java.util.List;

import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.entity.UserOrder;
import by.postnikov.rentbike.exception.DAOException;

/**
 * Data access interface that work with {@link User} entity
 *
 * @author Serge Postnikov
 */

public interface UserDAO {

	/**
	 * Search user in DB
	 *
	 * @param password - password of User that should be extracted from database
	 * @param user - object User with login of User that should be extracted from database
	 * @return User object from DB
	 * @throws DaoException if any SQL exception occur //TODO
	 */
	void login(User user, char[] password) throws DAOException;

	/**
	 * Add User in DB
	 *
	 * @param user - User object that should be saved in database
	 * @return string if user with such login or e-mail already exists
	 * @throws DaoException if any SQL exception occur //TODO
	 */
	String register(User user, char[] password) throws DAOException;

	/**
	 * Create bike order in DB
	 *
	 * @param order - BikeOrder object that should be saved in database
	 * @return BikeOrder object from DB with DB' ID auto generated
	 * @throws DAOException if any SQL exception occur
	 */
	BikeOrder createOrder(BikeOrder order) throws DAOException;
	
	
	/**
	 * Close bike order in DB
	 *
	 * @param orderId - current order id
	 * @param finishParkingId - finish parking id
	 * @param finishTime - order closing time
	 * @param payment - BigDecimal order's payment
	 * @throws DAOException if any SQL exception occur
	 */	
	void closeOrder(long orderId, long finishParkingId, String finishTime, BigDecimal payment) throws DAOException;
	
	
	/**
	 * Find open user's order in DB
	 *
	 * @param user - User object that should be saved in database
	 * @return true if open order exists or false if not
	 * @throws DAOException if any SQL exception occur
	 */	
	BikeOrder findOpenOrder(User user) throws DAOException;
	
	
	/**
	 * @param orderId
	 * @return
	 * @throws DAOException
	 */
	void findOpenOrderById(long orderId, BikeOrder bikeOrder) throws DAOException; 
	
	/**
	 * @return
	 * @throws DAOException
	 */
	List<UserOrder> takeAllUsers() throws DAOException;
	
	/**
	 * @param user
	 * @return
	 * @throws DAOException
	 */
	String updateUser (User user) throws DAOException;
	
	/**
	 * @param password
	 * @param user
	 * @throws DAOException
	 */
	void updatePassword(char[] password, User user) throws DAOException;
	
	
	List<BikeOrder> findAllOrderByUser(long userId) throws DAOException;
	
}
