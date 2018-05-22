package by.postnikov.rentbike.service;

import java.util.List;
import java.util.Map;

import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.ServiceException;

/**
 * @author Sergey Postnikov
 *
 */
public interface BikeService {
	
	/**
	 * Validates incoming parameters and adds bike.
	 * 
	 * @param requestParameters - all parameters from request
	 * @param bike - empty object of the Bike.
	 * @return notice both if validation failed or if bike with such combination of the brand and model already exist.
	 * @throws ServiceException if any exception occurred.
	 */
	String addBike(Map<String, String> requestParameters, Bike bike) throws ServiceException;

	/**
	 * Validates incoming parameters and adds brand.
	 * 
	 * @param brandName - brand name.
	 * @return notice both if validation failed or if same brand already exist.
	 * @throws ServiceException if any exception occurred.
	 */
	String addBrand(String brandName) throws ServiceException;

	/**
	 * Validates incoming parameters and adds bike type.
	 * 
	 * @param brandName - bike type.
	 * @return notice both if validation failed or if same type already exist.
	 * @throws ServiceException if any exception occurred.
	 */
	String addBikeType(String bikeType) throws ServiceException;
		
	/**
	 * Validates incoming parameters and adds bike products.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @param bikeProductList - empty list of bike products.
	 * @return notice if validation failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String addBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList) throws ServiceException;
	
	/**
	 * Returns all bike types.
	 * 
	 * @return list with bike types if those were found and empty list if not.
	 * @throws ServiceException if any exception occurred.
	 */
	List<BikeType> takeAllBikeType() throws ServiceException;

	/**
	 * Returns all brands.
	 * 
	 * @return list with brands if those were found and empty list if not.
	 * @throws ServiceException if any exception occurred.
	 */
	List<Brand> takeAllBrand() throws ServiceException;
	
	/**
	 * Validates incoming parameters and looks for a bike according to the received parameters. If bike were found, fills in the received empty bikeList with the found bikes.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @param bikeList - empty list.
	 * @param pageInfo - object with pagination parameters.
	 * @return notice if validation was failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String findBike(Map<String, String> requestParameters, List<Bike> bikeList, PageInfo pageInfo) throws ServiceException;
	
	/**
	 * Validates incoming parameter and get bike by id.
	 * 
	 * @param bikeId - as a String.
	 * @param bike - empty object of Bike.
	 * @return notice if validation was failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String takeBikeByID(String bikeId, Bike bike) throws ServiceException;
	
	/**
	 * Validates incoming parameters and looks for a bike products according to the received parameters. If bike products were found, fills in the received empty bikeProductList with the found bikes products.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @param bikeProductList - empty list.
	 * @param pageInfo - object with pagination parameters.
	 * @return notice if validation was failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String findBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList, PageInfo pageInfo) throws ServiceException;

	/**
	 * Validates incoming parameter and gets bike product by id. If bike product was found fills in fields of received bikeProduct.
	 * 
	 * @param bikeId as a String.
	 * @param bikeProduct - empty object of the BikeProduct.
	 * @return notice if validation was failed.
	 * @throws ServiceException if any exception occurred.
	 */
	String takeBikeProductById(String bikeId, BikeProduct bikeProduct) throws ServiceException;
	
	/**
	 * Validates incoming parameters and updates bike. If updating was successful, fills in fields of received bike.
	 * 
	 * @param requestParameters - all parameters from request.
	 * @param bike - empty object of Bike.
	 * @return notice both if validation failed or if bike with such combination of the brand and model already exist.
	 * @throws ServiceException if any exception occurred.
	 */
	String updateBike(Map<String, String> requestParameters, Bike bike) throws ServiceException;
	
}
