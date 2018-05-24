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
	 * Search user in DB. 
	 *
	 * @param user - object User with only login field that should be extracted from database
	 * @param password - password, which must be compared with the password from the database
	 * @return {@link User}.
	 * @throws DAOException if occurred SQL exception
	 */
	User login(User user, char[] password) throws DAOException;

	/**
	 * Add User to DB
	 *
	 * @param user - User object that should be added in database
	 * @param password as char[]
	 * @throws DAOException if occurred SQL exception
	 */
	void register(User user, char[] password) throws DAOException;

	/**
	 * Create bike order in DB
	 *
	 * @param order - BikeOrder object with empty id field, that should be added to database
	 * @return BikeOrder object from DB with auto generated bikeOrder ID
	 * @throws DAOException if occurred SQL exception
	 */
	BikeOrder createOrder(BikeOrder order) throws DAOException;
	
	
	/**
	 * Update bike order (close order) in DB
	 *
	 * @param orderId - order id, which will be closed
	 * @param finishParkingId - finish parking id
	 * @param finishTime - order closing time
	 * @param payment - BigDecimal order's payment
	 * @throws DAOException if occurred SQL exception
	 */	
	void closeOrder(long orderId, long finishParkingId, String finishTime, BigDecimal payment) throws DAOException;
	
	
	/**
	 * Find open user's order in DB
	 *
	 * @param user - User whose order will be searched
	 * @return object of BikeOrder, that was found for current user
	 * @throws DAOException if occurred SQL exception
	 */	
	BikeOrder findOpenOrder(User user) throws DAOException;
	
	
	/**
	 * Search for an open order by order id. If order is found, than fields of the received object of BikeOrder will be filled.
	 * 
	 * @param orderId
	 * @param bikeOrder - is empty object of BikeOrder
	 * @throws DAOException if occurred SQL exception
	 */
	void findOpenOrderById(long orderId, BikeOrder bikeOrder) throws DAOException; 
	
	/**
	 * The method returns a list of all users.
	 * 
	 * @return List<UserOrder> orderUserList
	 * @throws DAOException if occurred SQL exception
	 */
	List<UserOrder> takeAllUsers() throws DAOException;
	
	/**
	 * Update user fields except password
	 * 
	 * @param user - object of the User with new data
	 * @return String notice if new user login or e-mail already exist with other users
	 * @throws DAOException if occurred SQL exception
	 */
	String updateUser (User user) throws DAOException;
	
	/**
	 * Update user password
	 * 
	 * @param password
	 * @param user
	 * @throws DAOException if occurred SQL exception
	 */
	void updatePassword(char[] password, User user) throws DAOException;
	
	
	/**
	 * The method returns all orders of the user
	 * 
	 * @param userId
	 * @return List<BikeOrder> bikeOrderList
	 * @throws DAOException if occurred SQL exception
	 */
	List<BikeOrder> findAllOrderByUser(long userId) throws DAOException;
	
}
