package soundcloud.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SoundCloudStarter {

	public static void main(String[] args) {
		try {
			System.out.println("Start Server");
			ServerSocket serv = new ServerSocket(9090);
			System.out.println("Start Server Socket");
			Socket socket_serv = serv.accept();
			
			BufferedReader read = new BufferedReader(
									new InputStreamReader(socket_serv.getInputStream())
									);
			String line;
			while((line = read.readLine())!=null){
				System.out.println(line);
			}
			socket_serv.close();
			serv.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
