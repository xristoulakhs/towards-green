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
				
				// Post request types //
				
				// GETMOREPOSTS: Get more posts
				if (request.getRequestType().equals("GETMOREPOSTS")) {
					ArrayList<String> posts = postDao.getFirstN(2, Integer.parseInt(request.getContent()));
					String post = gson.toJson(posts);
					objectOS.writeObject(post);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				// GETPOSTS: Get posts
				if (request.getRequestType().equals("GETPOSTS")) {
					ArrayList<String> posts = postDao.getFirstN(Integer.parseInt(request.getContent()));
					String post = gson.toJson(posts);
					System.out.println(post);
					objectOS.writeObject(post);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				// INPOST: Insert post
				if (request.getRequestType().equals("INPOST")) {
					String json = request.getContent();
					boolean result = postDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// UPEV: Update event
				if (request.getRequestType().equals("UPPOST")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					boolean result = postDao.update( json[0], json[1]);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// DELPOST: Delete post
				if (request.getRequestType().equals("DELPOST")) {
					String json = request.getContent();
					boolean result = postDao.delete(json);
					System.out.println(result);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// UPPOSTWR: Update post without response
				if (request.getRequestType().equals("UPPOSTWR")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					boolean result = postDao.update( json[0], json[1]);
				}
				
				// Event request types //
				
				// GETMOREEV: Get more events
				if (request.getRequestType().equals("GETMOREEV")) {
					ArrayList<String> events = eventDao.getFirstN(2, Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				// GETEV: Get events
				if (request.getRequestType().equals("GETEV")) {
					ArrayList<String> events = eventDao.getFirstN(Integer.parseInt(request.getContent()));
					String event = gson.toJson(events);
					objectOS.writeObject(event);
					objectOS.flush();
					System.out.println(">Server: sending Request reply");
				}
				
				// UPEV: Update event
				if (request.getRequestType().equals("UPEV")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					boolean result = eventDao.update( json[0], json[1]);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// UPEV: Update event without response
				if (request.getRequestType().equals("UPEVWR")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					System.out.println(">Server: updating event record" + json[0] + "...");
					eventDao.update( json[0], json[1]);
				}
				
				// UPCLEV: Update closed event
				// When event has closed, we want to reward all the attendees with a badge, if there
				// is one.
				if (request.getRequestType().equals("UPCLEV")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					boolean result = eventDao.update( json[0], json[1]);
					
					Event event = gson.fromJson(json[1], Event.class);
					HashMap<String, Boolean> eventAttendees = event.getAttendees();
					
					Badge badge = event.getBadge();
					
					if (badge != null) {
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
					}
					
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
					
				}
				
				// INEV: Insert event
				if (request.getRequestType().equals("INEV")) {
					String json = request.getContent();
					boolean result = eventDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// DELEV: Delete event
				if (request.getRequestType().equals("DELEV")) {
					String json = request.getContent();
					boolean result = eventDao.delete(json);
					System.out.println(result);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// USERCON: User connection
				// User authentication
				if (request.getRequestType().equals("USERCON")) {
					String json = request.getContent();
					User user = gson.fromJson(json, User.class);
					String email = user.getEmail();
					String password = user.getPassword();
					String profileJson = profileDao.getFirstWithEmail(email);
					
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
				
				// Profile request types //
				
				// GETPR: Get profile
				if (request.getRequestType().equals("GETPR")) {
					String email = request.getContent();
					String json = profileDao.getFirstWithEmail(email);
					ArrayList<String> response = new ArrayList<String>();
					response.add(json);
					objectOS.writeObject(gson.toJson(response));
					objectOS.flush();
					
				}
				
				// INPR: Insert profile
				if (request.getRequestType().equals("INPR")) {
					String json = request.getContent();
					boolean result = profileDao.insert(json);
					System.out.println(">Server: insert was " + result);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				if (request.getRequestType().equals("GETSORTPROF")) {
					ArrayList<String> jsons = profileDao.getAllSortedByPoints();
					objectOS.writeObject(gson.toJson(jsons));
					objectOS.flush();
				}
				
				// REWARDPR: Reward profile
				if (request.getRequestType().equals("REWARDPR")) {
					String[] json = gson.fromJson(request.getContent(), String[].class);
					
					String jsonProfile = profileDao.getFirst(json[0]);
					Profile profile = gson.fromJson(jsonProfile, Profile.class);
					profile.addPoints(Integer.parseInt(json[1]));
					System.out.println(json[1]);
					profileDao.update(json[0], gson.toJson(profile));
				}
				
				// Badge request types //
				
				// INBDG: Insert badge
				if (request.getRequestType().equals("INBDG")) {
					String json = request.getContent();
					boolean result = badgeDao.insert(json);
					Request responseRequest = new Request("", gson.toJson(result));
					objectOS.writeObject(responseRequest);
					objectOS.flush();
				}
				
				// GETALLBDG: Get all badges
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
}