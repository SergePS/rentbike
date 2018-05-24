package by.postnikov.rentbike.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import by.postnikov.rentbike.entity.BikeOrder;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.User;
import by.postnikov.rentbike.entity.UserOrder;
import by.postnikov.rentbike.exception.ServiceException;


/**
 * @author Sergey Postnikov
 *
 */
public interface UserService {
	
	
	/**
	 * Validates incoming data and return {@link User} if login and password are valid or null if not.
	 * 
	 * @param requestParameters - all parameters from request (without password).
	 * @param password - char array object.
	 * @return {@link User}.
	 * @throws ServiceException if any exceptions occurred.
	 */
	User login(Map<String, String> requestParameters, char[] password) throws ServiceException;
	
	
	/**
	 * Validate incoming data and register new User.
	 * 
	 * @param requestParameters - all parameters from request (without password).
	 * @param password - char array object.
	 * @return {@link User}.
	 * @throws ServiceException if any exceptions occurred.
	 */
	void register(Map<String, String> requestParameters, char[] password) throws ServiceException;
	
	
	/**
	 * Create new bikeOrder {@link BikeOrder}
	 * 
	 * @param filled object of the {@link User}
	 * @param filled object of the BikeProduct 
	 * @return filled object of the BikeOrder
	 * @throws ServiceException
	 */
	BikeOrder createOrder(User user, BikeProduct bikeProduct) throws ServiceException;
	
	
	/**
	 * Find an open BikeOrder by User 
	 * 
	 * @param filled object of {@link User}
	 * @return filled BikeOrder if opened order exist or null if not 
	 * @throws ServiceException
	 */
	BikeOrder findOpenOrder(User user) throws ServiceException;
	
	
	/**
	 * Validates incoming data and returns {@link BikeOrder}. If bike order wasn't found - returns <code>null</code>.
	 * 
	 * @param Stirng orderId.
	 * @return {@link BikeOrder}.
	 * @throws ServiceException if any Exception occured.
	 */
	BikeOrder findOpenOrderById(String orderId) throws ServiceException;	//TODO delete method
	
	/**
	 * Close an open order
	 * 
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	BigDecimal closeOrder(Map<String, String> requestParameters) throws ServiceException;
	
	
	/**
	 * Return all UserOrder that contain User and BikeOrder 
	 * 
	 * @return List<UserOrder> whit orders if they exist and empty List if not 
	 * @throws ServiceException if any Exception occurred.
	 */
	List<UserOrder> takeAllUsers() throws ServiceException; 
	
	/**
	 * Validates incoming data and updates user personal data except password.
	 * 
	 * @param requestParameters - all parameters from request (without password).
	 * @param user {@link User} with current personal data.
	 * @return {@link User}
	 * @throws ServiceException if any Exception occurred.
	 */
	User userUpdate(Map<String, String> requestParameters, User user) throws ServiceException;
	
	
	/**
	 * Validates incoming data, checks old password and updates it.
	 * 
	 * @param currentPassword - current user password.
	 * @param currentPassword - new password.
	 * @param user {@link User}.
	 * @throws ServiceException if any Exception occurred.
	 */
	void updatePassword(char[] currentPassword, char[] password, User user) throws ServiceException;
	
	/**
	 * Returns a list of user orders.
	 * 
	 * @param user {@link User}.
	 * @return List<BikeOrder>. 
	 * @throws ServiceException if any Exception occurred.
	 */
	List<BikeOrder> findAllOrderByUser (User user) throws ServiceException; 
	
}
