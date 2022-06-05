import java.util.ArrayList;
import com.mongodb.BasicDBObject;

public class BadgeDao implements Dao {
	private static BadgeDao badgeDao = null;
	private MongoDB mongoDB;
	
	private BadgeDao() {
		this.mongoDB = new MongoDB("Badge");
	}
	
	public static BadgeDao getInstance() {
		if (badgeDao == null) {
			badgeDao = new BadgeDao();
		}
		return badgeDao;
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
		BasicDBObject query = new BasicDBObject("badgeID", id);
		String record = this.mongoDB.getFirst(query);
		return record;
	}

	//@Override
	public boolean insert(String event) {
		return this.mongoDB.insert(event);
	}

	@Override
	public boolean update(String id, String updatedEvent) {
		BasicDBObject query = new BasicDBObject("badgeID", id);
		BasicDBObject updateRecord = BasicDBObject.parse(updatedEvent);
		return this.mongoDB.update(query, updateRecord);
		
	}

	@Override
	public boolean delete(String id) {
		BasicDBObject query = new BasicDBObject("badgeID", id);
		return this.mongoDB.delete(query);
	}
}
