package by.postnikov.rentbike.dao;

import java.util.List;

import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.BikeProductState;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.exception.DAOException;

public interface BikeDAO {
	
	
	/**
	 * Add new Bike to DB
	 * 
	 * @param bike BIke object that should be added to database
	 * @return notice if bike with such combination of the brand and model already exist
	 * @throws DAOException if occurred SQL exception 
	 */
	String addBike(Bike bike) throws DAOException;
	
	/**
	 * Add Brand to DB
	 * 
	 * @param brand Brand object that should be added to database
	 * @return error message if current brand name already exist
	 * @throws DAOException if occurred SQL exception 
	 */
	String addBrand(Brand brand) throws DAOException;
	
	/**
	 * Add BikeType to DB
	 * 
	 * @param bikeType BikeType object that should be added to DB
	 * @return error message if current Bike Type already exist
	 * @throws DAOException if occurred SQL exception
	 */
	String addBikeType(BikeType bikeType) throws DAOException;
	
	/**
	 * Add BikeProducts to DB. Add DB auto generated ID to every BikeProduct if they was added. 
	 * 
	 * @param bikeProductList
	 * @throws DAOException if occurred SQL exception
	 */
	void addBikeProduct(List<BikeProduct> bikeProductList) throws DAOException;
	
	
	/**
	 * Get All BikeType from DB
	 * 
	 * @return List<BikeType> bikeTypeList
	 * @throws DAOException if occurred SQL exception
	 */
	List<BikeType> takeAllBikeType() throws DAOException;
	
	/**
	 * Get all Brands
	 * 
	 * @return List<Brand> brand
	 * @throws DAOException if occurred SQL exception
	 */
	List<Brand> takeAllBrand() throws DAOException;
	
	/**
	 * The method fills all fields of the received Bike object if it was found by ID.
	 * 
	 * @param bikeId
	 * @param bike - empty object of the Bike
	 * @throws DAOException if occurred SQL exception
	 */
	void takeBikeById(long bikeId, Bike bike) throws DAOException;
	
	/**
	 * Method looks for a bikes from the received parameters. If bikes was found, fills the received empty collection with the found bikes
	 * 
	 * @param bikeList empty collection of the bikes
	 * @param brandId
	 * @param bikeTypeId
	 * @param model
	 * @param minSpeedCount
	 * @param maxSpeedCount
	 * @param pageInfo of the PageInfo with parameters for the LIMIT operator
	 * @throws DAOException if occurred SQL exception
	 */
	void findBike(List<Bike> bikeList, long brandId, long bikeTypeId, String model, int minSpeedCount, int maxSpeedCount, PageInfo pageInfo) throws DAOException;
	
	/**
	 * Looks for a bike products according to the received parameters. If bike products were found, fills in the received empty collection with the found bike products.
	 * 
	 * @param parkingId
	 * @param brandId
	 * @param bikeTypeId
	 * @param model
	 * @param state
	 * @param bikeList - empty collection of the bike products
	 * @param pageInfo of the PageInfo with parameters for the LIMIT operator
	 * @throws DAOException if occurred SQL exception
	 */
	void findBikeProduct(long parkingId, long brandId, long bikeTypeId, String model, BikeProductState state, List<BikeProduct> bikeProductList, PageInfo pageInfo) throws DAOException;
	
	/**
	 * Looks for bike product by received id and fills in bike product fields if it was found.
	 * 
	 * @param bikeId
	 * @param bikeProduct - empty object of BikeProduct
	 * @throws DAOException if occurred SQL exception
	 */
	void takeBikeProductById(long bikeId, BikeProduct bikeProduct) throws DAOException;
	
	/**
	 * Method updates bike
	 * 
	 * @param bike
	 * @param return String notice if new combination of parameters already exist with other Bike 
	 * @throws DAOException if occurred SQL exception
	 */
	String updateBike(Bike bike) throws DAOException;
	
	
}
