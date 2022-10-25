package socketTestServer;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import config.ServerInfo;

public class FileReceiveThread implements Runnable {

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private ServerInfo serverInfo;
	private ServerLogger serverLogger;

	public FileReceiveThread(Socket socket, ServerInfo serverInfo) {
		this.socket = socket;
		this.serverInfo = serverInfo;
		serverLogger=ServerLogger.getInstance();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataInputStream dis = null;
		FileOutputStream fos = null;
		System.out.println("thread run@@");
		int calcSize = 0;
		String[] fileInfo = null;
		File saveFile = null;
		try {
			is = socket.getInputStream();
			serverLogger.fine(this.getClass().getSimpleName(), "socket.getInputStream");
			os = socket.getOutputStream();
			serverLogger.fine(this.getClass().getSimpleName(), "socket.getOutputStream");
			dis = new DataInputStream(new BufferedInputStream(is));
			serverLogger.fine(this.getClass().getSimpleName(), "newDataInputStream");
			

			String fileNS = "";
			fileNS = dis.readUTF();
			serverLogger.fine(this.getClass().getSimpleName(), "read_"+fileNS);
			
			
			System.out.println(fileNS);
			fileInfo = fileNS.split("/");

			String path = serverInfo.getSaveDirectory().getPath()+File.separator + fileInfo[0];
			saveFile = new File(path);
			String newFilename = "";
			if (saveFile.exists()) {
				newFilename = changeFilename(saveFile.getName());
				path = serverInfo.getSaveDirectory().getPath()+File.separator + newFilename;
				saveFile = new File(path);
			}
			System.out.println("saveFile!!    " + saveFile.getPath());
			fos = new FileOutputStream(saveFile);
			int n = 0;

			byte[] buffer = new byte[1024];
			while (true) { // (n = fis.read(buffer)) != -1 파일에서 읽을 때 -1일을 받는 거지 클라이언트에서 보낸걸 받을 때는 -1을 받지않음!
							// 대기가 걸려잇음
				n = dis.read(buffer);
				fos.write(buffer, 0, n);
				fos.flush();
				calcSize += n;
				if (calcSize == Integer.parseInt(fileInfo[1])) {
					break;
				}
			}
			serverLogger.info("FileReceiveThread", saveFile.getName() + ">>receivecomplete");

		} catch (IOException e) {
			serverLogger.severe("FileReceiveThread", e.getMessage());
			if (calcSize != Integer.parseInt(fileInfo[1])) {
				if (saveFile != null) {
					if (saveFile.exists()) {
						saveFile.delete();
					}
				}
			}
		
		}finally {
		  if(dis != null) {try {dis.close();} catch (IOException e) {serverLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
		  if(fos != null) {try {fos.close();} catch (IOException e) {serverLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
		  if(is != null) {try {is.close();} catch (IOException e) {serverLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
		  if(os != null) {try {os.close();} catch (IOException e) {serverLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
		  try {if(socket!=null) {if(!socket.isClosed()) {socket.close();}}} catch (IOException e) {serverLogger.severe(this.getClass().getSimpleName(), e.getMessage());}
		}

	}

	// 쓰레드가 동시에 중복 확인하는 것때문에 숫자 증가하는 건 안될듯??
	public String changeFilename(String filename) {
		String[] filenameArr = filename.split("[.]");
		String newName = "";
		String randomNum = "";
		for (int i = 0; i < 4; i++) {
			randomNum += (int) (Math.random() * 10);
		}
		newName = filenameArr[0] + "_" + randomNum + "." + filenameArr[1];
		return newName;
	}

}
