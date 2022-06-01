import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static Server server = null;
	
	private Server() {
		
	}
	
	public static Server getInstance() {
		if (server == null) {
			server = new Server();
		}
		return server;
	}
	
	public void openServer() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		
		try {
			serverSocket = new ServerSocket(8080);
			System.out.println(">Server: running");
			System.out.println(">Server: waiting for connections");
			while (true) {
				socket = serverSocket.accept();
				System.out.println(">Server: new connection");
				ActionsForServer actionsForServer = new ActionsForServer(socket);
				actionsForServer.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main (String[] args) {
		Server.getInstance().openServer();
	}
}
