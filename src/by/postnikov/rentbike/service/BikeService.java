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
	 * @param requestParameters
	 * @param bike
	 * @return
	 * @throws ServiceException
	 */
	String addBike(Map<String, String> requestParameters, Bike bike) throws ServiceException;

	/**
	 * @param brand
	 * @return
	 * @throws ServiceException
	 */
	String addBrand(Map<String, String> requestParameters, Brand brand) throws ServiceException;

	/**
	 * @param bikeType
	 * @return
	 * @throws ServiceException
	 */
	String addBikeType(String bikeType) throws ServiceException;
	
	/**
	 * @param requestParameters
	 * @return
	 */
	String addBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList) throws ServiceException;
	

	/**
	 * @return
	 * @throws ServiceException
	 */
	List<BikeType> takeAllBikeType() throws ServiceException;

	/**
	 * @return
	 * @throws ServiceException
	 */
	List<Brand> takeAllBrand() throws ServiceException;
	
	/**
	 * @param requestParameters
	 * @return
	 * @throws ServiceException
	 */
	String findBike(Map<String, String> requestParameters, List<Bike> bikeList, PageInfo pageInfo) throws ServiceException;
	
	
	/**
	 * @param bikeId
	 * @return
	 * @throws ServiceException
	 */
	String takeBikeByID(String bikeId, Bike bike) throws ServiceException;
	
	/**
	 * @param requestParameters
	 * @return
	 * @throws ServiceException
	 */
	String findBikeProduct(Map<String, String> requestParameters, List<BikeProduct> bikeProductList, PageInfo pageInfo) throws ServiceException;
	
	/**
	 * @param bikeId
	 * @return
	 * @throws ServiceException
	 */
	String takeBikeProductById(String bikeId, BikeProduct bikeProduct) throws ServiceException;
	
	
	/**
	 * @param requestParameters
	 * @param bike
	 * @return
	 * @throws ServiceException
	 */
	String updateBike(Map<String, String> requestParameters, Bike bike) throws ServiceException;
	
}
