package by.postnikov.rentbike.connection;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPool {

	private static Logger logger = LogManager.getLogger();
	
	private ConnectionPoolSlaveDeamon connectionPoolSlaveDeamon;

	private static AtomicBoolean isPoolCreated = new AtomicBoolean(false);
	private static Lock lock = new ReentrantLock();

	private static ConnectionPool pool;
	private static Properties properties = DBProperty.takeProperty();

	private final static String DEFAULT_CONNECTION_COUNT_KEY = "connectionCount";
	private final static int DEFAULT_WRAPPER_CONNECTION_COUNT = Integer
			.parseInt(properties.getProperty(DEFAULT_CONNECTION_COUNT_KEY));

	private final static String MAX_WRAPPER_CONNECTION_COUNT_KEY = "maxConnectionCount";
	private final static int MAX_WRAPPER_CONNECTION_COUNT = Integer
			.parseInt(properties.getProperty(MAX_WRAPPER_CONNECTION_COUNT_KEY));

	private static int currentWrapperConnectionCount = DEFAULT_WRAPPER_CONNECTION_COUNT;

	private final static String WAIT_TIME_CONNECTTION_KEY = "waitTimeConnection";
	private final static int WAIT_TIME_CONNECTTION = Integer
			.parseInt(properties.getProperty(WAIT_TIME_CONNECTTION_KEY));

	private static boolean isConnectionPoolWork;

	private static BlockingQueue<WrapperConnection> wrapperConnectionQueue;

	private ConnectionPool() {
		connectionPoolSlaveDeamon = new ConnectionPoolSlaveDeamon();
		connectionPoolSlaveDeamon.start();
	}

	/**
	 * Method that create connection pool
	 * 
	 * @return ConnectionPool pool
	 */
	public static ConnectionPool getInstance() {
		if (!isPoolCreated.get()) {
			lock.lock();
			if (!isPoolCreated.get()) {
				pool = new ConnectionPool();
				wrapperConnectionCreate();
				isPoolCreated.set(true);
			}
			lock.unlock();
		}
		return pool;
	}

	/**
	 * Method gives out wrapperConnection
	 * 
	 * @return WrapperConnection wrapperConnection
	 */
	public WrapperConnection getWrapperConnection() {
		WrapperConnection wrapperConnection;

		try {
			wrapperConnection = wrapperConnectionQueue.poll(WAIT_TIME_CONNECTTION, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "Get connection error, " + e.getMessage());
			throw new RuntimeException(); // stub
		}

		if (!wrapperConnection.isValidConnection()) {
			logger.log(Level.ERROR,
					"When the connection was issued, was found that it was bad, so old connection terminated and new connection created");
			wrapperConnection.closeConnection();
			wrapperConnection = createConnection();
		}
		return wrapperConnection;
	}

	/**
	 * Method takes back wrapperConnection.
	 * 
	 * @param wrapperConnection
	 *            - returned WrapperConnection
	 */
	public void returnWrapperConnection(WrapperConnection wrapperConnection) {
		if (wrapperConnection != null) {
			try {
				if (!wrapperConnection.isValidConnection()) {
					logger.log(Level.ERROR,
							"When the connection was obtained, was found that it was bad, so old connection terminated and new connection created");
					wrapperConnection.closeConnection();
					wrapperConnection = createConnection();
				}
				wrapperConnection.setAutoCommit(true);
				wrapperConnectionQueue.put(wrapperConnection);
			} catch (InterruptedException e) {
				logger.log(Level.ERROR, "Cannot add wraperConnection in Pool, " + e.getMessage());
			}
		}
	}

	/**
	 * Method close all connection from connection pool.
	 */
	public void closeAllConnection() {
		int availableConnection = wrapperConnectionQueue.size();
		for (int i = 0; i < availableConnection; i++) {
			try {
				wrapperConnectionQueue.poll(WAIT_TIME_CONNECTTION, TimeUnit.MILLISECONDS).closeConnection();
			} catch (InterruptedException e) {
				logger.log(Level.ERROR, "Close connection error, " + e.getMessage());
			}
		}

		if (availableConnection == currentWrapperConnectionCount) {
			logger.log(Level.DEBUG, "All connection were closed");
		} else {
			logger.log(Level.ERROR, "Not all connection were closed");
		}

		isConnectionPoolWork = false;

		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				logger.log(Level.DEBUG, "All drivers deregistred");
			} catch (SQLException e) {
				logger.log(Level.ERROR, "Drivers deregister error");
			}
		}
		
		connectionPoolSlaveDeamon.setStopWorkThread();

	}

	boolean isConnectionPoolWork() {
		return isConnectionPoolWork;
	}

	int getAvailableConnectionCount() {
		return wrapperConnectionQueue.size();
	}

	void increaseWripperConnectionCount() {
		if (currentWrapperConnectionCount < MAX_WRAPPER_CONNECTION_COUNT) {
			try {
				WrapperConnection wrapperConnection = new WrapperConnection(properties);
				wrapperConnectionQueue.put(wrapperConnection);
				logger.log(Level.DEBUG, "One wrapperConnection added");
			} catch (InterruptedException e) {
				logger.log(Level.ERROR, "WrapperConnection didn't create, " + e.getMessage());
			}
		}
	}

	void decreaseWripperConnectionCount() {
		if (currentWrapperConnectionCount > DEFAULT_WRAPPER_CONNECTION_COUNT) {
			try {
				wrapperConnectionQueue.poll(WAIT_TIME_CONNECTTION, TimeUnit.MILLISECONDS).closeConnection();
				logger.log(Level.DEBUG, "One wrapperConnection closed");
			} catch (InterruptedException e) {
				logger.log(Level.ERROR, "WrapperConnection didn't close" + e.getMessage());
			}
		}
	}

	private static void wrapperConnectionCreate() {

		wrapperConnectionQueue = new LinkedBlockingQueue<WrapperConnection>();

		try {
			Class.forName(properties.getProperty("JDBCDriver"));
			WrapperConnection wrapperConnection;

			for (int i = 0; i < DEFAULT_WRAPPER_CONNECTION_COUNT; i++) {
				wrapperConnection = new WrapperConnection(properties);
				wrapperConnectionQueue.put(wrapperConnection);
			}
			logger.log(Level.DEBUG, DEFAULT_WRAPPER_CONNECTION_COUNT + " connection were create");
		} catch (ClassNotFoundException e) {
			logger.log(Level.FATAL, "JDBC Drive didn't found, " + e.getMessage());
			throw new RuntimeException();
		} catch (InterruptedException e) {
			logger.log(Level.ERROR, "Cannot add wraperConnection in Pool, " + e.getMessage());
		}

		isConnectionPoolWork = true;
	}

	private WrapperConnection createConnection() {
		return new WrapperConnection(properties);
	}

}
