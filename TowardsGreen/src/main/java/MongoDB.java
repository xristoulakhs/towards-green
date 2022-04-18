import java.util.Iterator;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.google.gson.Gson;

public class MongoDB {

	private static MongoDB mongoInstance = null;
	private MongoClient mongoClient;
	private String uri;
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	
	private MongoDB() {
		this.uri = "mongodb+srv://Apipilikas:p3180157agg15@cluster.r0jin.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
		
		//using try this way, the connection is terminated
		try (MongoClient mongoClient = MongoClients.create(uri)) {
			this.mongoClient = mongoClient;
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection("users");
			
			
			//Event event = new Event("e1","aggelos");
			Gson gson = new Gson();
			//Document doc = Document.parse(gson.toJson(event));
			//this.collection.insertOne(doc);
			
			
			//System.out.println(gson.fromJson(gson.toJson(event), Event.class).getReactions().get("Reaction 2"));
			//BasicDBObject bs = new BasicDBObject("test","This is a test");
			Document doc = this.collection.find().first();
			doc.remove("_id");
			System.out.println(doc.toJson());
			Event event = gson.fromJson(doc.toJson(), Event.class);
			System.out.println(event.getCreator());
			//System.out.println(doc.iterator().next());
		}
	}
	
	public static MongoDB getMongoDBInstance() {
		if (mongoInstance == null) {
			mongoInstance = new MongoDB();
		}
		return mongoInstance;
	}
	
	public void printAllRecords() {
		FindIterable<Document> doc = this.collection.find();
		Iterator iter = doc.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
	
	public static void main(String[] args) {
		MongoDB mongo = MongoDB.getMongoDBInstance();
		//mongo.printAllRecords();
		//BasicDBObject bs = new BasicDBObject("test","This is a test");
		Event event = new Event("e1","aggelos");
		Gson gson = new Gson();
		System.out.println(gson.toJson(event));
		System.out.println(gson.fromJson(gson.toJson(event), Event.class).getCreator());

		
	}
}
