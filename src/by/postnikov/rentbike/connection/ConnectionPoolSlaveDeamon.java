package by.postnikov.rentbike.connection;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolSlaveDeamon extends Thread {

	private static Logger logger = LogManager.getLogger();
	
	private static boolean workThread = true;
	
	private final static String THREAD_NAME = "GhostThread";
	
	ConnectionPoolSlaveDeamon() {
		this.setDaemon(true);
		this.setName(THREAD_NAME);
		logger.log(Level.DEBUG, "GhostThread has started");
	}
	

	@Override
	public void run() {
		ConnectionPool connectionPool = ConnectionPool.getInstance();
		
		int counter = 0;
		
		while(workThread) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
				int connectionCount = connectionPool.getAvailableConnectionCount();
				if(connectionCount < 2) {
					counter++;
				} else {
					counter = 0;
				}
				if(counter!=0 & counter%100 == 0 & connectionPool.isConnectionPoolWork()) {
					logger.log(Level.DEBUG, "Available connection count = " + connectionPool.getAvailableConnectionCount());
				}
			} catch (InterruptedException e) {
				logger.log(Level.DEBUG, "ConnectionPoolSlaveDeamon error, " + e.getMessage());
			}
		}
		
		logger.log(Level.DEBUG, "GhostThread stopped");
		
	}
	
	public void setStopWorkThread() {
		workThread = false;
	}

}
