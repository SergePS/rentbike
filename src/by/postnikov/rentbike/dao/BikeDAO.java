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
	 * Add Bike in DB
	 * 
	 * @param bike BIke object that should be saved in database
	 * @return error message 'error' if Bike with current Brand and Model already exist
	 * @throws DAOException if occurred SQL exception 
	 */
	String addBike(Bike bike) throws DAOException;
	
	/**
	 * Add Brand in DB
	 * 
	 * @param brand Brand object that should be saved in database
	 * @return error message 'error' if current brand name already exist
	 * @throws DAOException if occurred SQL exception 
	 */
	String addBrand(Brand brand) throws DAOException;
	
	/**
	 * Add BikeType in DB
	 * 
	 * @param bikeType
	 * @return
	 * @throws DAOException if occurred SQL exception
	 */
	String addBikeType(BikeType bikeType) throws DAOException;
	
	/**
	 * @param bikeProductList
	 * @return
	 */
	void addBikeProduct(List<BikeProduct> bikeProductList) throws DAOException;
	
	
	/**
	 * Take All BikeType from DB
	 * @return
	 * @throws DAOException
	 */
	List<BikeType> takeAllBikeType() throws DAOException;
	
	/**
	 * @return
	 * @throws DAOException
	 */
	List<Brand> takeAllBrand() throws DAOException;
	
	/**
	 * @param bikeId
	 * @return
	 * @throws DAOException
	 */
	void takeBikeById(long bikeId, Bike bike) throws DAOException;
	
	
	
	/**
	 * @param brandId
	 * @param bikeTypeId
	 * @param model
	 * @param minSpeedCount
	 * @param maxSpeedCount
	 * @return
	 * @throws DAOException
	 */
	void findBike(List<Bike> bikeList, long brandId, long bikeTypeId, String model, int minSpeedCount, int maxSpeedCount, PageInfo pageInfo) throws DAOException;
	
	/**
	 * @param parkingId
	 * @param brandId
	 * @param bikeTypeId
	 * @param model
	 * @param state
	 * @return
	 * @throws DAOException
	 */
	void findBikeProduct(long parkingId, long brandId, long bikeTypeId, String model, BikeProductState state, List<BikeProduct> bikeProductList, PageInfo pageInfo) throws DAOException;
	
	/**
	 * @param bikeId
	 * @return
	 * @throws DAOException
	 */
	void takeBikeProductById(long bikeId, BikeProduct bikeProduct) throws DAOException;
	
	/**
	 * @param bike
	 * @throws DAOException
	 */
	String updateBike(Bike bike) throws DAOException;
	
	
}
