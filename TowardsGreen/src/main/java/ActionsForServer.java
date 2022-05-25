import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.aueb.towardsgreen.Request;
import com.google.gson.Gson;

public class ActionsForServer extends Thread {
	Gson gson = new Gson();
	ObjectInputStream objectIS;
	ObjectOutputStream objectOS;
	
	public ActionsForServer(Socket socket) {
		try {
			this.objectOS = new ObjectOutputStream(socket.getOutputStream());
			this.objectIS = new ObjectInputStream(socket.getInputStream());
			System.out.println("hi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		EventDao eventDao = EventDao.getInstance();
		
		while (true) {
			try {
				System.out.println(">Server: waiting for request...");
				Request request = (Request) objectIS.readObject();
				System.out.println(">Server: got a new Request with type " + request.getRequestType());
				
				if (request.getRequestType().equals("GET")) {
					ArrayList<String> events = eventDao.getFirstN(2, Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("GET2")) {
					ArrayList<String> events = eventDao.getFirstN(Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("UP")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					boolean result = eventDao.update(json[0], json[1]);
					System.out.println(">Server: update was " + result);
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}	
//	public static void main(String[] args) {
//		//EventDao eventDao = EventDao.getInstance();
//		ObjectOutputStream objectOS = null;
//		try {
//			ServerSocket serverSocket = new ServerSocket(8080);
//			Socket socket = serverSocket.accept();
//			//Gson gson = new Gson();
//			System.out.println("Server got a connection");
//			//String event = gson.toJson(eventDao.getAll());
//			//System.out.println(event);
//			//String event = "{\"eventID\": \"e101\", \"creator\": \"aggelos_dokimi\", \"publishedDate\": \"2022-05-17\", \"meetingDate\": \"2022-05-17\", \"publishedTime\": \"23:09\", \"meetingTime\": \"23:09\", \"status\": \"OPEN\", \"title\": \"Testing!\", \"description\": \"This is a test!\", \"meetingLocation\": \"Somewhere\", \"reactions\": {\"TakePart\": 3}, \"badge\": \"b101\"}";
//			//System.out.println(event);
//			//Request request = new Request("DOKIMI","CONTENT");
//			objectOS = new ObjectOutputStream(socket.getOutputStream());
//			//
//			//Event ev = gson.fromJson(event, Event.class);
//			//System.out.println(ev.getMeetingTime().toString());
//			//objectOS.writeObject(event);
//			//objectOS.flush();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		ActionsForServer action = new ActionsForServer(objectOS);
//		action.start();
//	}
}