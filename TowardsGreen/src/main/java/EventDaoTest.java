// Just for testing reasons
import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.Gson;

public class EventDaoTest {
	public static void main(String[] args) throws IOException {
		// Insert
		EventDao eventDao = EventDao.getInstance();
		Gson gson = new Gson();
		Event event1 = new Event("e101", "Γιώργος");
		eventDao.insert(gson.toJson(event1));
//		Event event2 = new Event("e102", "aggelos_dokimi");
//		eventDao.insert(event2);
//		Event event3 = new Event("e103", "aggelos_dokimi");
//		eventDao.insert(event3);
		
		// Get All
//		ArrayList<Event> events = eventDao.getAll();
//		
//		for (Event event:events) {
//			System.out.println(event.getEventID());
//		}
		
		// Get All with parameter
//		ArrayList<Event> events = eventDao.getAll("e101");
//		
//		for (Event event:events) {
//			System.out.println(event.getEventID());
//		}
		//System.out.println(eventDao.getFirstN(1,1).get(0));
		
//		// Get first
//		Event event = eventDao.getFirst();
//		System.out.println(event.getEventID());
		
		// Get first with parameter
//		Event event = eventDao.getFirst("e103");
//		System.out.println(event.getMeetingDate().getDayOfMonth());
		
		// Update
//		Event evupd = new Event("e105","updated");
//		eventDao.update("e101", evupd);
//		Event event = eventDao.getFirst("e105");
//		System.out.println(event.getCreator());
		
		// Delete
		//eventDao.delete("e105");
	}
}
