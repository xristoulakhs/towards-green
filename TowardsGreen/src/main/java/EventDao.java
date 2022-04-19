import java.util.ArrayList;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;

public class EventDao implements Dao<Event> {
	private static EventDao eventDao = null;
	private MongoDB<Event> mongoDB;
	private Gson gson = new Gson();
	
	private EventDao() {
		this.mongoDB = new MongoDB<Event>(Event.class);
		//System.out.println("Created");
	}
	
	public static EventDao getInstance() {
		if (eventDao == null) {
			eventDao = new EventDao();
		}
		return eventDao;
	}

	@Override
	public ArrayList<Event> getAll() {
		ArrayList<String> records = this.mongoDB.getAll();
		ArrayList<Event> events = convertJsonToEvent(records);
		return events;
	}

	@Override
	public ArrayList<Event> getAll(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		ArrayList<String> records = this.mongoDB.getAll(query);
		ArrayList<Event> events = convertJsonToEvent(records);
		return events;
	}
	
	@Override
	public Event getFirst() {
		String record = this.mongoDB.getFirst();
		Event event = this.gson.fromJson(record, Event.class);
		return event;
	}
	
	@Override
	public Event getFirst(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		String record = this.mongoDB.getFirst(query);
		Event event = this.gson.fromJson(record, Event.class);
		return event;
	}

	@Override
	public void insert(Event event) {
		this.mongoDB.insert(this.gson.toJson(event));
	}

	@Override
	public void update(String id, Event updatedEvent) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		String json = this.gson.toJson(updatedEvent);
		BasicDBObject updateRecord = BasicDBObject.parse(json);
		this.mongoDB.update(query, updateRecord);
		
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		this.mongoDB.delete(query);
	}
	
	public ArrayList<Event> convertJsonToEvent(ArrayList<String> records) {
		ArrayList<Event> events = new ArrayList<>();
		
		for (String record:records) {
			Event event = this.gson.fromJson(record, Event.class);
			events.add(event);
		}
		return events;
	}
	
	public static void main(String[] args) {
		EventDao eventDao = EventDao.getInstance();
		Event event = new Event("e101", "aggelos_dokimi");
		eventDao.insert(event);
		//System.out.println(eventDao.getFirst().getCreator());
	}
	
}
