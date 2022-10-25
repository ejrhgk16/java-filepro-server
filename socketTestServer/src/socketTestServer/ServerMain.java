package socketTestServer;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import config.ServerInfo;

public class ServerMain {

	public static void main(String[]args) {
		ServerSocket serverSocket = null;
		ExecutorService es = null;
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.readConfig();
		ServerLogger logger = ServerLogger.getInstance();
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(serverInfo.getServerPort()));
			es = Executors.newFixedThreadPool(serverInfo.getThreadNum());
			while(true) {
				System.out.println("요청대기중");
				Socket socket = serverSocket.accept();
				logger.info("Main", socket.getInetAddress()+"_accept");
				FileReceiveThread ft = new FileReceiveThread(socket, serverInfo); 
				es.submit(ft);
				System.out.println("new thread");
			}
		} catch (IOException e) {
			logger.severe("Main", e.getMessage());
		}finally {
			es.shutdown();
			try {
				if(serverSocket != null) {
					serverSocket.close();
				}
			} catch (IOException e) {
				logger.severe("Main", e.getMessage());
			}
		}

		
	}
}
