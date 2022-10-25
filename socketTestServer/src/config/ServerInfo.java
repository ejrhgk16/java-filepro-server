package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import socketTestServer.ServerLogger;

public class ServerInfo {
	private int serverPort;
	private int threadNum;
	private File saveDirectory;
	private String logPath;
	private ServerLogger serverLogger;

	public void readConfig() {
		Properties properties = new Properties();
		String path = ServerInfo.class.getResource("ServerConfig.properties").getPath();
		try {
			path = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			properties.load(new FileReader(path));
			serverPort = Integer.parseInt(properties.getProperty("ServerPort"));
			threadNum = Integer.parseInt(properties.getProperty("ThreadNum"));
			String fileSavePath = properties.getProperty("FileSavePath");
			saveDirectory = new File(fileSavePath);
			checkDirectory(saveDirectory);
			System.out.println("deriectorydsfa " + saveDirectory.getPath() );
			
			logPath = properties.getProperty("LogPath");
			String logLevel = properties.getProperty("LogLevel");
			System.out.println(logLevel);
			serverLogger = ServerLogger.getInstance();
			serverLogger.setLogConfig(logLevel, logPath);
			
			File logDirect = new File(logPath);
			checkDirectory(logDirect);

		} catch (FileNotFoundException e) {
			serverLogger.severe("ServerInfo", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			serverLogger.severe("ServerInfo", e.getMessage());
			e.printStackTrace();
		}
	}

	void checkDirectory(File f) {
		if (f.exists() == false) {
			f.mkdir();
		} else {
			if (f.isDirectory() == false) {
				f.mkdir();
			}
		}
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public File getSaveDirectory() {
		return saveDirectory;
	}

	public String getLogPath() {
		return logPath;
	}

}
