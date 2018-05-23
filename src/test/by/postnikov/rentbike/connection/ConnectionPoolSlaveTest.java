package test.by.postnikov.rentbike.connection;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import by.postnikov.rentbike.connection.ConnectionPool;
import by.postnikov.rentbike.dao.DAOFactory;
import by.postnikov.rentbike.dao.UserDAO;
import by.postnikov.rentbike.exception.DAOException;

public class ConnectionPoolSlaveTest {

	@BeforeClass
	public void beforeClass() {

	}

	@AfterClass
	public void afterClass() {
		ConnectionPool.getInstance().closeAllWrapperConnection();
	}

	@Test
	public void run() { // load test

		ThreadTest threadTestOne = new ThreadTest();
		threadTestOne.setName("threadTestOne");
		threadTestOne.start();

		ThreadTest threadTestTwo = new ThreadTest();
		threadTestTwo.setName("threadTestTwo");
		threadTestTwo.start();

		ThreadTest threadTestThree = new ThreadTest();
		threadTestThree.setName("threadTestThree");
		threadTestThree.start();

		ThreadTest threadTestFour = new ThreadTest();
		threadTestFour.setName("threadTestFour");
		threadTestFour.start();

		try {
			threadTestOne.join();
			threadTestTwo.join();
			threadTestThree.join();
			threadTestFour.join();

			TimeUnit.SECONDS.sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

class ThreadTest extends Thread {
	private static Logger logger = LogManager.getLogger();

	private static DAOFactory daoFactory = DAOFactory.getInstance();
	private static UserDAO userDAO = daoFactory.getUserDAO();

	@Override
	public void run() {

		logger.log(Level.DEBUG, this.getName() + " launched");

		loadingCycle();

		try {
			TimeUnit.MILLISECONDS.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		loadingCycle();

		logger.log(Level.DEBUG, this.getName() + " stopped");

	}

	private void loadingCycle() {
		for (long stop = System.nanoTime() + TimeUnit.SECONDS.toNanos(4); stop > System.nanoTime();) {
			try {
				userDAO.takeAllUsers();
			} catch (DAOException e) {
				e.printStackTrace();
			}
		}
	}

}
