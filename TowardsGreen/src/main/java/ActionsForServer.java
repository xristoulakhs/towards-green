import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.aueb.towardsgreen.*;
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
		PostDao postDao = PostDao.getInstance();
		EventDao eventDao = EventDao.getInstance();
		ProfileDao profileDao = ProfileDao.getInstance();
		BadgeDao badgeDao = BadgeDao.getInstance();
		
		while (true) {
			try {
				System.out.println(">Server: waiting for request...");
				Request request = (Request) objectIS.readObject();
				System.out.println(">Server: got a new Request with type " + request.getRequestType());
				
				// Post request types
				if (request.getRequestType().equals("GETMOREPOSTS")) {
					ArrayList<String> posts = postDao.getFirstN(2, Integer.parseInt(request.getContent()));
					String post = gson.toJson(posts);
					objectOS.writeObject(post);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("GETPOSTS")) {
					ArrayList<String> posts = postDao.getFirstN(Integer.parseInt(request.getContent()));
					String post = gson.toJson(posts);
					System.out.println(post);
					objectOS.writeObject(post);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("INPOST")) {
					String json = request.getContent();
					boolean result = postDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// Event request types
				if (request.getRequestType().equals("GETMOREEV")) {
					ArrayList<String> events = eventDao.getFirstN(2, Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("GETEV")) {
					ArrayList<String> events = eventDao.getFirstN(Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				if (request.getRequestType().equals("UPEV")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					boolean result = eventDao.update( json[0], json[1]);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				if (request.getRequestType().equals("UPEVWR")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					boolean result = eventDao.update( json[0], json[1]);
				}
				
				if (request.getRequestType().equals("UPCLEV")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					boolean result = eventDao.update( json[0], json[1]);
					
					Event event = gson.fromJson(json[1], Event.class);
					HashMap<String, Boolean> eventAttendees = event.getAttendees();
					
					Badge badge = event.getBadge();
					int badgePoints = badge.getPointsEarned();
					
					for (Map.Entry<String, Boolean> attendee : eventAttendees.entrySet()) {
						if (attendee.getValue()) {
							String attendeeProfileID = attendee.getKey();
							String jsonProfile = profileDao.getFirst(attendeeProfileID);
							
							Profile profile = gson.fromJson(jsonProfile, Profile.class);
							profile.getBadges().add(badge);
							profile.addPoints(badgePoints);
							
							profileDao.update(attendeeProfileID, gson.toJson(profile));
						}
					}
					
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
					
				}
				
				if (request.getRequestType().equals("INEV")) {
					String json = request.getContent();
					boolean result = eventDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				if (request.getRequestType().equals("DELEV")) {
					String json = request.getContent();
					boolean result = eventDao.delete(json);
					System.out.println(result);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// User authentication
				if (request.getRequestType().equals("USERCON")) {
					String json = request.getContent();
					User user = gson.fromJson(json, User.class);
					String email = user.getEmail();
					String password = user.getPassword();
					String profileJson = profileDao.getFirstWithEmail(email);
					// Authentication happens here. How? Email and password checked with database
					// If they match send true, otherwise false.
					// Attention: We will use the email to Authenticate!
					boolean result = false;
					if (profileJson != null) {
						Profile profile = gson.fromJson(profileJson, Profile.class);
						String emailFromDB = profile.getEmail();
						String passwordFromDB = profile.getPassword();
						if (emailFromDB.equals(email) && passwordFromDB.equals(password)) {
							result = true;
						}
					}
					
					Request responseRequest = new Request("USERCONRESP", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// Profile request types
				if (request.getRequestType().equals("GETPR")) {
					String email = request.getContent();
					String json = profileDao.getFirstWithEmail(email);
					ArrayList<String> response = new ArrayList<String>();
					response.add(json);
					objectOS.writeObject(gson.toJson(response));
					objectOS.flush();
					
				}
				
				if (request.getRequestType().equals("INPR")) {
					String json = request.getContent();
					boolean result = profileDao.insert(json);
					System.out.println(">Server: insert was " + result);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// Badge request types
				if (request.getRequestType().equals("INBDG")) {
					String json = request.getContent();
					boolean result = badgeDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				if (request.getRequestType().equals("GETALLBDG")) {
					ArrayList<String> jsons = badgeDao.getAll();
					objectOS.writeObject(gson.toJson(jsons));
					objectOS.flush();
					
				}
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}	
	}
	
	private ArrayList<Profile> convertJsonToProfiles(ArrayList<String> jsons) {
		ArrayList<Profile> profiles = new ArrayList<Profile>();
		for (String json : jsons) {
			profiles.add(gson.fromJson(json, Profile.class));
		}
		return profiles;
	}
	
	private void updateProfiles(HashMap<String, Boolean> eventAttendees, Badge badge) {
		int badgePoints = badge.getPointsEarned();
		for (Map.Entry<String, Boolean> attendee : eventAttendees.entrySet()) {
			if (attendee.getValue()) {
				
			}
		}
	}
}