// Just for testing reasons
import java.util.ArrayList;

public class EventDaoTest {
	public static void main(String[] args) {
		// Insert
		EventDao eventDao = EventDao.getInstance();
//		Event event1 = new Event("e101", "aggelos_dokimi");
//		eventDao.insert(event1);
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
		eventDao.delete("e105");
	}
}
