import java.util.ArrayList;

public class EventDao implements Dao<Event> {
	
	private static EventDao eventDao = null;
	
	private EventDao() {
		
	}
	
	public static EventDao getInstance() {
		if (eventDao == null) {
			eventDao = new EventDao();
		}
		return eventDao;
	}

	@Override
	public ArrayList<Event> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(Event obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub
		
	}
	
	
}
