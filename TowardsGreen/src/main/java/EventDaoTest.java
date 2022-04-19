import com.google.gson.Gson;

public class EventDaoTest {
	public static void main(String[] args) {
		Gson gson = new Gson();
		//EventDao eventDao = EventDao.getInstance();
		Event event = new Event("e101", "aggelos_dokimi");
		//eventDao.insert(event);
		MongoDB<Event> mongo = new MongoDB<Event>(Event.class);
		mongo.insert(gson.toJson(event));
	}
}
