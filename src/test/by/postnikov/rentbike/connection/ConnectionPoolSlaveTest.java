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
	public void run() throws InterruptedException { // load test

		Thread[] threads = new Thread[4];

		for (int i = 0; i < threads.length; i++) {
			threads[i] = new ThreadTest();
			threads[i].setName("Thread_" + (i + 1));
			threads[i].start();
		}

		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}

		TimeUnit.SECONDS.sleep(3);

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
