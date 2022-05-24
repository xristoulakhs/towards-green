//import java.util.ArrayList;
//
//import com.google.gson.Gson;
//import com.mongodb.BasicDBObject;
//
//public class EventDao implements Dao<Event> {
//	private static EventDao eventDao = null;
//	private MongoDB<Event> mongoDB;
//	private Gson gson = new Gson();
//	
//	private EventDao() {
//		this.mongoDB = new MongoDB<Event>(Event.class);
//	}
//	
//	public static EventDao getInstance() {
//		if (eventDao == null) {
//			eventDao = new EventDao();
//		}
//		return eventDao;
//	}
//
//	@Override
//	public ArrayList<Event> getAll() {
//		ArrayList<String> records = this.mongoDB.getAll();
//		ArrayList<Event> events = convertJsonToEvent(records);
//		return events;
//	}
//
//	@Override
//	public ArrayList<Event> getAll(String id) {
//		BasicDBObject query = new BasicDBObject("eventID", id);
//		ArrayList<String> records = this.mongoDB.getAll(query);
//		ArrayList<Event> events = convertJsonToEvent(records);
//		return events;
//	}
//	
//	@Override
//	public Event getFirst() {
//		String record = this.mongoDB.getFirst();
//		Event event = this.gson.fromJson(record, Event.class);
//		return event;
//	}
//	
//	@Override
//	public Event getFirst(String id) {
//		BasicDBObject query = new BasicDBObject("eventID", id);
//		String record = this.mongoDB.getFirst(query);
//		Event event = this.gson.fromJson(record, Event.class);
//		return event;
//	}
//
//	//@Override
//	public void insert(Event event) {
//		this.mongoDB.insert(this.gson.toJson(event));
//	}
//
//	@Override
//	public void update(String id, Event updatedEvent) {
//		BasicDBObject query = new BasicDBObject("eventID", id);
//		String json = this.gson.toJson(updatedEvent);
//		BasicDBObject updateRecord = BasicDBObject.parse(json);
//		this.mongoDB.update(query, updateRecord);
//		
//	}
//
//	@Override
//	public void delete(String id) {
//		BasicDBObject query = new BasicDBObject("eventID", id);
//		this.mongoDB.delete(query);
//	}
//	
//	public ArrayList<Event> convertJsonToEvent(ArrayList<String> records) {
//		ArrayList<Event> events = new ArrayList<>();
//		
//		for (String record:records) {
//			Event event = this.gson.fromJson(record, Event.class);
//			events.add(event);
//		}
//		return events;
//	}
//}

// Version 2

import java.util.ArrayList;
import com.mongodb.BasicDBObject;

public class EventDao implements Dao {
	private static EventDao eventDao = null;
	private MongoDB<Event> mongoDB;
	
	private EventDao() {
		this.mongoDB = new MongoDB<Event>(Event.class);
	}
	
	public static EventDao getInstance() {
		if (eventDao == null) {
			eventDao = new EventDao();
		}
		return eventDao;
	}

	@Override
	public ArrayList<String> getAll() {
		return this.mongoDB.getAll();
	}

	@Override
	public ArrayList<String> getAll(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		return this.mongoDB.getAll(query);
	}
	
	@Override
	public ArrayList<String> getFirstN(int limit) {
		return this.mongoDB.getFirstN(limit);
	}

	@Override
	public ArrayList<String> getFirstN(int limit, int skip) {
		return this.mongoDB.getFirstN(limit, skip);
	}
	
	@Override
	public String getFirst() {
		return this.mongoDB.getFirst();
	}
	
	@Override
	public String getFirst(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		String record = this.mongoDB.getFirst(query);
		return record;
	}

	//@Override
	public void insert(String event) {
		this.mongoDB.insert(event);
	}

	@Override
	public void update(String id, String updatedEvent) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		BasicDBObject updateRecord = BasicDBObject.parse(updatedEvent);
		this.mongoDB.update(query, updateRecord);
		
	}

	@Override
	public void delete(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		this.mongoDB.delete(query);
	}
}
