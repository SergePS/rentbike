package by.postnikov.rentbike.service;

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
	 * Validate incoming data and return the filled object of {@link User} if login and password are valid
	 * 
	 * @param requestParameters - all parameters from request (without password)
	 * @param password - char array object
	 * @param user - empty object of the {@link User}
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String login(Map<String, String> requestParameters, char[] password, User user) throws ServiceException;
	
	
	/**
	 * Validate incoming data and register new User 
	 * 
	 * @param requestParameters - all parameters from request (without password)
	 * @param password - char array object
	 * @param user - empty object of the {@link User}
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String register(Map<String, String> requestParameters, char[] password, User user) throws ServiceException;
	
	
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
	 * Method validates incoming data and fills the received empty BikeOrder object if it is found by orderId
	 * 
	 * @param Stirng orderId
	 * @param bikeOrder - empty object or the BikeOrder
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String findOpenOrderById(String orderId, BikeOrder bikeOrder) throws ServiceException;
	
	/**
	 * Close an open order
	 * 
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String closeOrder(Map<String, String> requestParameters) throws ServiceException;
	
	
	/**
	 * Return all UserOrder that contain User and BikeOrder 
	 * 
	 * @return List<UserOrder> whit orders if they exist and empty List if not 
	 * @throws ServiceException
	 */
	List<UserOrder> takeAllUsers() throws ServiceException; 
	
	/**
	 * Method validates incoming data and updates userData without password
	 * 
	 * @param requestParameters - all parameters from request (without password)
	 * @param user with new data
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String userUpdate(Map<String, String> requestParameters, User user) throws ServiceException;
	
	
	/**
	 * Method validates incoming data, checks old password and updates it 
	 * 
	 * @param currentPassword - current user password
	 * @param currentPassword - new password
	 * @param user
	 * @return empty String if the action succeeds or String message of the MessagePage if something wrong
	 * @throws ServiceException
	 */
	String updatePassword(char[] currentPassword, char[] password, User user) throws ServiceException;
	
	/**
	 * Method returns a list of orders if they exist and null if not
	 * 
	 * @param user
	 * @return List<BikeOrder> with BikeOrders if order exists for 
	 * @throws ServiceException
	 */
	List<BikeOrder> findAllOrderByUser (User user) throws ServiceException; 
	
}
