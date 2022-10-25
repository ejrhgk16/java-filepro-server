package socketTestServer;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class ServerLogger {
	private static ServerLogger serverLogger;
	private Logger logger;
	private FileHandler filehandler;

	private ServerLogger() {

	}

	public static synchronized ServerLogger getInstance() {
		if (serverLogger == null) {
			serverLogger = new ServerLogger();
		}
		return serverLogger;
	}

	public void fine(String className, String msg) {
		logger.fine(className+"/ "+msg);
	}
	
	public void info(String className, String msg) {
		logger.info(className+"/ "+msg);
	}
	
	public void severe(String className, String msg) {
		logger.severe(className+"/ "+msg);
	}

	public void setLogConfig(String logLevel, String logPath) {

		try {
			filehandler = new FileHandler(logPath + "/serverLog%g.txt", 20000, 1000, true);
			SimpleFormatter simpleFormatter = new SimpleFormatter();
			filehandler.setFormatter(simpleFormatter);
			logger = Logger.getLogger("ServerLogger");
			logger.addHandler(filehandler);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		if (logLevel.equals("FINE")) {
			Logger.getLogger("").setLevel(Level.FINE);
			filehandler.setLevel(Level.FINE);
		} else if (logLevel.equals("INFO")) {
			filehandler.setLevel(Level.INFO);
		} else if (logLevel.equals("SEVERE")) {
			filehandler.setLevel(Level.SEVERE);
			

		}
	}

}
