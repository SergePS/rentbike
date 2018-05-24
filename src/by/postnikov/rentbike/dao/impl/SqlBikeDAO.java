package by.postnikov.rentbike.dao.impl;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import by.postnikov.rentbike.command.PageInfo;
import by.postnikov.rentbike.connection.ConnectionPool;
import by.postnikov.rentbike.connection.WrapperConnection;
import by.postnikov.rentbike.dao.BikeDAO;
import by.postnikov.rentbike.dao.DateFormatting;
import by.postnikov.rentbike.entity.Bike;
import by.postnikov.rentbike.entity.BikeProduct;
import by.postnikov.rentbike.entity.BikeProductState;
import by.postnikov.rentbike.entity.BikeType;
import by.postnikov.rentbike.entity.Brand;
import by.postnikov.rentbike.entity.Parking;
import by.postnikov.rentbike.exception.DAOException;
import by.postnikov.rentbike.exception.ExceptionMessage;

public class SqlBikeDAO implements BikeDAO {

	public final static int PARAMETER_1 = 1;

	private final static String TAKE_ALL_BIKE_TYPE = "SELECT id, type FROM biketype";

	private final static String TAKE_ALL_BRAND = "SELECT id, brand FROM brands";

	private final static String ADD_BIKE = "INSERT INTO bikes (brandId, model, wheelSize, speedCount, bikeTypeId, picture) VALUES (?, ?, ?, ?, ?, ?)";
	private final static int BIKE_BRAND_ID_PARAM = 1;
	private final static int BIKE_MODEL_PARAM = 2;
	private final static int BIKE_WHEEL_SIZE_PARAM = 3;
	private final static int BIKE_SPEED_COUNT_PARAM = 4;
	private final static int BIKE_BIKE_TYPE_ID_PARAM = 5;
	private final static int BIKE_PICTURE_PARAM = 6;

	private final static String ADD_BRAND = "INSERT INTO brands (brand) VALUES (?)";

	private final static String ADD_BIKE_TYPE = "INSERT INTO biketype (type) VALUES (?)";
	private final static int BIKE_TYPE_RS_ADDRESS_BIKE_TYPE_TABLE = 1;

	private static final String FIND_BIKE_FB = "{call findBikeProcedure(?,?,?,?,?,?,?)}";
	private static final int FB_BRAND_ID = 1; // FB - Find Bike
	private static final int FB_MODEL = 2;
	private static final int FB_BIKE_TYPE_ID = 3;
	private static final int FB_SPEED_COUNT_MIN = 4;
	private static final int FB_SPEED_COUNT_MAX = 5;
	private static final int FB_START_ID = 6;
	private static final int FB_COUNT_ELEMENT_ON_PAGE = 7;

	private final static String FIND_BIKE_BY_ID = "SELECT bk.id, bk.brandId, br.brand, bk.model, bk.wheelSize, bk.speedCount, bk.picture, bk.bikeTypeId, bt.type FROM bikes bk LEFT JOIN brands br ON bk.brandId = br.id LEFT JOIN biketype bt ON bk.bikeTypeId = bt.id WHERE bk.id = ?";
	private final static int ID_PARAM = 1;

	private final static String ADD_BIKE_PRODUCT = "INSERT INTO bikeproduct (bikeId, purchaseDate, value, rentPrice, parkingId) VALUES (?, ?, ?, ?, ?)";
	private final static int ADD_BIKE_PRODUCT_ID_BIKE_ADDRESS = 1;
	private final static int ADD_BIKE_PRODUCT_PURCHASE_DATE_ADDRESS = 2;
	private final static int ADD_BIKE_PRODUCT_VLUE_ADDRESS = 3;
	private final static int ADD_BIKE_PRODUCT_RENT_PRICE_ADDRESS = 4;
	private final static int ADD_BIKE_PRODUCT_PARKING_ID_ADDRESS = 5;

	private final static String FIND_BIKE_PRODUCT_FBP = "{call findBikeProductProcedure(?,?,?,?,?,?,?)}";
	private static final int FBP_PRODUCT_STATE = 1; // FBP - Find Bike Product
	private static final int FBP_PARKING_ID = 2;
	private static final int FBP_BRAND_ID = 3;
	private static final int FBP_BIKE_TYPE_ID = 4;
	private static final int FBP_MODEL = 5;
	private static final int FBP_START_ID = 6;
	private static final int FBP_COUNT_ELEMENT_ON_PAGE = 7;

	private final static String FIND_BIKE_PRODUCT_BY_ID = "SELECT bp.id, bk.id AS bikeId, bk.brandId, br.brand, bk.model, bk.wheelSize, bk.speedCount, bk.picture, bk.bikeTypeId, bt.type, pk.id AS parkingId, pk.address, bp.value, bp.rentPrice, bp.state FROM bikeproduct bp LEFT JOIN bikes bk ON bp.bikeId=bk.id LEFT JOIN brands br ON bk.brandId = br.id LEFT JOIN bikeType bt ON bk.bikeTypeId = bt.id LEFT JOIN parkings pk ON bp.parkingId=pk.id WHERE bp.id = ?";

	private final static String SQL_QUERY_UPDATE_BIKE = "UPDATE bikes SET brandId = ?, model = ?, wheelSize = ?, speedCount = ?, bikeTypeId = ?, picture = ? WHERE id = ?";
	private final static int BIKE_UPDATED_ID_PARAM = 7;

	public final static String WHERE = " where ";
	public final static String AND = " and ";

	private final static String ID = "id";
	private final static String TYPE = "type";
	private final static String BRAND = "brand";
	private final static String BRAND_ID = "brandId";
	private final static String MODEL = "model";
	private final static String WHEELSIZE = "wheelSize";
	private final static String SPEEDCOUNT = "speedCount";
	private final static String PICTURE = "picture";
	private final static String BIKE_TYPE_ID = "biketypeId";
	private final static String VALUE = "value";
	private final static String RENT_PRICE = "rentPrice";
	private final static String PARKING_ID = "parkingId";
	private final static String ADDRESS = "address";
	private final static String STATE = "state";
	private final static String BIKE_ID = "bikeId";

	@Override
	public List<BikeType> takeAllBikeType() throws DAOException {

		List<BikeType> bikeTypeList = new ArrayList<>();

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		Statement statement = wrapperConnection.getStatement();

		try {
			ResultSet resultSet = statement.executeQuery(TAKE_ALL_BIKE_TYPE);

			while (resultSet.next()) {
				BikeType bikeType = new BikeType();
				bikeType.setId(resultSet.getInt(ID));
				bikeType.setBikeType(resultSet.getString(TYPE));
				bikeTypeList.add(bikeType);
			}

		} catch (SQLException e) {
			throw new DAOException("Get bike type error", e);
		} finally {
			wrapperConnection.closeStatement(statement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return bikeTypeList;
	}

	@Override
	public List<Brand> takeAllBrand() throws DAOException {

		Brand brand;
		List<Brand> brandList = new ArrayList<>();

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		Statement statement = wrapperConnection.getStatement();

		try {
			ResultSet resultSet = statement.executeQuery(TAKE_ALL_BRAND);

			while (resultSet.next()) {
				brand = new Brand();
				brand.setId(resultSet.getInt(ID));
				brand.setBrand(resultSet.getString(BRAND));
				brandList.add(brand);
			}

		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while getting all brands from the DB", e);
		} finally {
			wrapperConnection.closeStatement(statement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

		return brandList;
	}

	@Override
	public Bike addBike(Bike bike) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(ADD_BIKE);

		try {
			preparedStatement.setLong(BIKE_BRAND_ID_PARAM, bike.getBrand().getId());
			preparedStatement.setString(BIKE_MODEL_PARAM, bike.getModel());
			preparedStatement.setInt(BIKE_WHEEL_SIZE_PARAM, bike.getWheelSize());
			preparedStatement.setInt(BIKE_SPEED_COUNT_PARAM, bike.getSpeedCount());
			preparedStatement.setLong(BIKE_BIKE_TYPE_ID_PARAM, bike.getBikeType().getId());
			preparedStatement.setString(BIKE_PICTURE_PARAM, bike.getPicturePath());

			preparedStatement.executeUpdate();

			long lastId = ((com.mysql.jdbc.PreparedStatement) preparedStatement).getLastInsertID();
			bike.setId(lastId);

			return bike;

		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new DAOException(ExceptionMessage.BIKE_DUBLICATE_ERROR.toString());
		} catch (SQLException e) {
			throw new DAOException("Add bike error", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public void addBrand(Brand brand) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(ADD_BRAND);

		try {
			preparedStatement.setString(BIKE_BRAND_ID_PARAM, brand.getBrand());
			preparedStatement.executeUpdate();

		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new DAOException(ExceptionMessage.BRAND_DUBLICATE_ERROR.toString());
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while adding brand to the DB", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public void addBikeType(BikeType bikeType) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(ADD_BIKE_TYPE);

		try {
			preparedStatement.setString(BIKE_TYPE_RS_ADDRESS_BIKE_TYPE_TABLE, bikeType.getBikeType());
			preparedStatement.executeUpdate();
		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new DAOException(ExceptionMessage.BIKE_TYPE_DUBLICATE_ERROR.toString());
		} catch (SQLException e) {
			throw new DAOException("Exception occured during add bike type to DB", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public List<Bike> findBike(long brandId, long bikeTypeId, String model, int minSpeedCount, int maxSpeedCount,
			PageInfo pageInfo) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		CallableStatement callableStatement = wrapperConnection.getCallableStatement(FIND_BIKE_FB);

		try {
			callableStatement.setLong(FB_BRAND_ID, brandId);
			callableStatement.setString(FB_MODEL, model);
			callableStatement.setLong(FB_BIKE_TYPE_ID, bikeTypeId);
			callableStatement.setInt(FB_SPEED_COUNT_MIN, minSpeedCount);
			callableStatement.setInt(FB_SPEED_COUNT_MAX, maxSpeedCount);
			callableStatement.setLong(FB_START_ID, pageInfo.getLastPagePoint());
			callableStatement.setInt(FB_COUNT_ELEMENT_ON_PAGE, pageInfo.getDefaultElementOnPage());

			ResultSet resultSet = callableStatement.executeQuery();

			List<Bike> bikeList = new ArrayList<>();

			while (resultSet.next()) {
				Bike bike = new Bike();
				bike.setId(resultSet.getLong(ID));

				Brand brand = new Brand();
				brand.setId(resultSet.getInt(BRAND_ID));
				brand.setBrand(resultSet.getString(BRAND));
				bike.setBrand(brand);

				bike.setModel(resultSet.getString(MODEL));

				bike.setWheelSize(resultSet.getInt(WHEELSIZE));
				bike.setSpeedCount(resultSet.getInt(SPEEDCOUNT));
				bike.setPicturePath(resultSet.getString(PICTURE));

				BikeType bikeType = new BikeType();
				bikeType.setId(resultSet.getInt(BIKE_TYPE_ID));
				bikeType.setBikeType(resultSet.getString(TYPE));
				bike.setBikeType(bikeType);

				bikeList.add(bike);
			}

			return bikeList;

		} catch (SQLException e) {
			throw new DAOException("Exception occured during find bike in DB", e);
		} finally {
			wrapperConnection.closeStatement(callableStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public Bike takeBikeById(long bikeId) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(FIND_BIKE_BY_ID);

		try {
			preparedStatement.setLong(ID_PARAM, bikeId);

			ResultSet resultSet = preparedStatement.executeQuery();

			Bike bike = null;
			if (resultSet.next()) {
				bike = new Bike();
				bike.setId(resultSet.getLong(ID));

				Brand brand = new Brand();
				brand.setId(resultSet.getInt(BRAND_ID));
				brand.setBrand(resultSet.getString(BRAND));
				bike.setBrand(brand);

				bike.setModel(resultSet.getString(MODEL));

				bike.setWheelSize(resultSet.getInt(WHEELSIZE));
				bike.setSpeedCount(resultSet.getInt(SPEEDCOUNT));
				bike.setPicturePath(resultSet.getString(PICTURE));

				BikeType bikeType = new BikeType();
				bikeType.setId(resultSet.getInt(BIKE_TYPE_ID));
				bikeType.setBikeType(resultSet.getString(TYPE));
				bike.setBikeType(bikeType);
			}

			return bike;
		} catch (SQLException e) {
			throw new DAOException("Get bike by id error", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public List<BikeProduct> addBikeProduct(List<BikeProduct> bikeProductList) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(ADD_BIKE_PRODUCT);

		try {
			for (BikeProduct bikeProduct : bikeProductList) {
				preparedStatement.setLong(ADD_BIKE_PRODUCT_ID_BIKE_ADDRESS, bikeProduct.getBike().getId());
				preparedStatement.setString(ADD_BIKE_PRODUCT_PURCHASE_DATE_ADDRESS, DateFormatting.getCurrentDate());
				preparedStatement.setBigDecimal(ADD_BIKE_PRODUCT_VLUE_ADDRESS, bikeProduct.getValue());
				preparedStatement.setBigDecimal(ADD_BIKE_PRODUCT_RENT_PRICE_ADDRESS, bikeProduct.getRentPrice());
				preparedStatement.setLong(ADD_BIKE_PRODUCT_PARKING_ID_ADDRESS, bikeProduct.getParking().getId());
				preparedStatement.executeUpdate();

				long lastId = ((com.mysql.jdbc.PreparedStatement) preparedStatement).getLastInsertID();
				bikeProduct.setId(lastId);
			}
			return bikeProductList;
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while adding bike product to the DB", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public List<BikeProduct> findBikeProduct(long parkingId, long brandId, long bikeTypeId, String model,
			BikeProductState state, PageInfo pageInfo) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		CallableStatement callableStatement = wrapperConnection.getCallableStatement(FIND_BIKE_PRODUCT_FBP);

		try {
			callableStatement.setString(FBP_PRODUCT_STATE, state.toString());
			callableStatement.setLong(FBP_PARKING_ID, parkingId);
			callableStatement.setLong(FBP_BRAND_ID, brandId);
			callableStatement.setLong(FBP_BIKE_TYPE_ID, bikeTypeId);
			callableStatement.setString(FBP_MODEL, model);
			callableStatement.setLong(FBP_START_ID, pageInfo.getLastPagePoint());
			callableStatement.setInt(FBP_COUNT_ELEMENT_ON_PAGE, pageInfo.getDefaultElementOnPage());

			ResultSet resultSet = callableStatement.executeQuery();

			List<BikeProduct> bikeProductList = new ArrayList<>();
			
			while (resultSet.next()) {
				BikeProduct bikeProduct = new BikeProduct();

				bikeProduct.setId(resultSet.getLong(ID));

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
				bikeType.setId(resultSet.getInt(BIKE_TYPE_ID));
				bikeType.setBikeType(resultSet.getString(TYPE));
				bike.setBikeType(bikeType);

				bikeProduct.setBike(bike);
				bikeProduct.setValue(resultSet.getBigDecimal(VALUE));
				bikeProduct.setRentPrice(resultSet.getBigDecimal(RENT_PRICE));

				Parking parking = new Parking();
				parking.setId(resultSet.getLong(PARKING_ID));
				parking.setAddress(resultSet.getString(ADDRESS));
				bikeProduct.setParking(parking);

				bikeProductList.add(bikeProduct);
			}
			
			return bikeProductList;

		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while finding bikeProduct", e);
		} finally {
			wrapperConnection.closeStatement(callableStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

	@Override
	public BikeProduct takeBikeProductById(long bikeId) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(FIND_BIKE_PRODUCT_BY_ID);

		try {
			preparedStatement.setLong(PARAMETER_1, bikeId);
			ResultSet resultSet = preparedStatement.executeQuery();

			BikeProduct bikeProduct = null;
			
			if (resultSet.next()) {
				bikeProduct = new BikeProduct();
				bikeProduct.setId(resultSet.getLong(ID));

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
				bikeType.setId(resultSet.getInt(BIKE_TYPE_ID));
				bikeType.setBikeType(resultSet.getString(TYPE));
				bike.setBikeType(bikeType);

				bikeProduct.setBike(bike);
				bikeProduct.setValue(resultSet.getBigDecimal(VALUE));
				bikeProduct.setRentPrice(resultSet.getBigDecimal(RENT_PRICE));
				bikeProduct.setState(BikeProductState.valueOf(resultSet.getString(STATE).toUpperCase()));

				Parking parking = new Parking();
				parking.setId(resultSet.getLong(PARKING_ID));
				parking.setAddress(resultSet.getString(ADDRESS));
				bikeProduct.setParking(parking);

			}
			
			return bikeProduct;
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while getting bike product by id", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}
	}

	@Override
	public Bike updateBike(Bike bike) throws DAOException {

		ConnectionPool connectionPool = ConnectionPool.getInstance();
		WrapperConnection wrapperConnection = connectionPool.getWrapperConnection();
		PreparedStatement preparedStatement = wrapperConnection.getPreparedStatement(SQL_QUERY_UPDATE_BIKE);

		try {
			preparedStatement.setLong(BIKE_BRAND_ID_PARAM, bike.getBrand().getId());
			preparedStatement.setString(BIKE_MODEL_PARAM, bike.getModel());
			preparedStatement.setInt(BIKE_WHEEL_SIZE_PARAM, bike.getWheelSize());
			preparedStatement.setInt(BIKE_SPEED_COUNT_PARAM, bike.getSpeedCount());
			preparedStatement.setLong(BIKE_BIKE_TYPE_ID_PARAM, bike.getBikeType().getId());
			preparedStatement.setString(BIKE_PICTURE_PARAM, bike.getPicturePath());
			preparedStatement.setLong(BIKE_UPDATED_ID_PARAM, bike.getId());

			preparedStatement.executeUpdate();

			return bike;

		} catch (MySQLIntegrityConstraintViolationException e) {
			throw new DAOException(ExceptionMessage.BIKE_DUBLICATE_ERROR.toString());
		} catch (SQLException e) {
			throw new DAOException("An exeption occured in the layer DAO while updating bike", e);
		} finally {
			wrapperConnection.closeStatement(preparedStatement);
			connectionPool.returnWrapperConnection(wrapperConnection);
		}

	}

}
