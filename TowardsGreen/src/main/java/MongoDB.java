import java.util.Iterator;

import org.bson.Document;

import com.mongodb.client.*;

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
			Document doc = this.collection.find().first();
			System.out.println(doc);
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
	}
}
