import java.util.ArrayList;
import com.mongodb.BasicDBObject;

public class EventDao implements Dao {
	private static EventDao eventDao = null;
	private MongoDB mongoDB;
	
	private EventDao() {
		this.mongoDB = new MongoDB("Event");
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
	public boolean insert(String event) {
		return this.mongoDB.insert(event);
	}

	@Override
	public boolean update(String id, String updatedEvent) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		BasicDBObject updateRecord = BasicDBObject.parse(updatedEvent);
		return this.mongoDB.update(query, updateRecord);
		
	}

	@Override
	public boolean delete(String id) {
		BasicDBObject query = new BasicDBObject("eventID", id);
		return this.mongoDB.delete(query);
	}
}
