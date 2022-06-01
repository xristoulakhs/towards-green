import java.util.ArrayList;
import org.bson.Document;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

public class MongoDB<T> {

	private String uri = "mongodb+srv://Apipilikas:p3180157agg15@cluster.r0jin.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
	private MongoDatabase database;
	private MongoCollection<Document> collection;
	private Class<T> type;
	private String collectionName;
	
	public MongoDB(Class<T> type) {
		this.type = type;
		if (this.type.equals(Event.class)) {
			this.collectionName = "events";
		}
	}
	
	/**
	* This method inserts a new record in the collection.
	* @param record JSON format of an object
	* @return acknowledgment, the result of inserting a record: success or failure
	*/
	public boolean insert(String record) {
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			System.out.println(record);
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			Document doc = Document.parse(record);
			System.out.println("Parsed!");
			InsertOneResult insertResult = this.collection.insertOne(doc);
			return insertResult.wasAcknowledged();
		}
	}
	
	/**
	 * This method deletes a record in the collection.
	 * @param queryTuple (variable, value) of the record we want to be deleted
	 * @return acknowledgment, the result of deleting a record: success or failure
	 */
	public boolean delete(BasicDBObject query) {
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			DeleteResult deleteResult = this.collection.deleteOne(query);
			return deleteResult.wasAcknowledged();
		}
	}
	
	/**
	 * This method gets all the records of the chosen collection.
	 * @return records String ArrayList (each String is actually JSON format of the record)
	 */
	public ArrayList<String> getAll() {
		ArrayList<String> records = new ArrayList<>();
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			FindIterable<Document> iterable = this.collection.find();
			ArrayList<Document> documents = new ArrayList<>();
			iterable.into(documents);
			
			for (Document doc:documents) {
				doc.remove("_id");
				records.add(doc.toJson());
			}
		}
		return records;
	}
	
	/**
	 * This method gets all the records of the chosen collection, given a specific query.
	 * @param queryTuple (variable, value) of the record/records we want to be returned
	 * @return records String ArrayList (each String is actually JSON format of the record)
	 */
	public ArrayList<String> getAll(BasicDBObject query) {
		ArrayList<String> records = new ArrayList<>();
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			FindIterable<Document> iterable = this.collection.find(query);
			ArrayList<Document> documents = new ArrayList<>();
			iterable.into(documents);
			
			for (Document doc:documents) {
				doc.remove("_id");
				records.add(doc.toJson());
			}
		}
		return records;
	}
	
	/**
	 * This method gets all the first n records of the chosen collection.
	 * @param limitNumber of the first record/records we want to be returned
	 * @return records String ArrayList (each String is actually JSON format of the record)
	 */
	public ArrayList<String> getFirstN(int limit) {
		ArrayList<String> records = new ArrayList<>();
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			FindIterable<Document> iterable = this.collection.find().limit(limit);
			ArrayList<Document> documents = new ArrayList<>();
			iterable.into(documents);
			
			for (Document doc:documents) {
				doc.remove("_id");
				records.add(doc.toJson());
			}
		}
		return records;
	}
	
	/**
	 * This method gets all the first n records of the chosen collection.
	 * @param limitNumber of the first record/records we want to be returned
	 * @param skipNumber of the record/records we want to be skipped
	 * @return records String ArrayList (each String is actually JSON format of the record)
	 */
	public ArrayList<String> getFirstN(int limit, int skip) {
		ArrayList<String> records = new ArrayList<>();
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			FindIterable<Document> iterable = this.collection.find().limit(limit).skip(skip);
			ArrayList<Document> documents = new ArrayList<>();
			iterable.into(documents);
			
			for (Document doc:documents) {
				doc.remove("_id");
				records.add(doc.toJson());
			}
		}
		return records;
	}
	
	/**
	 * This method gets only the first record of the collection.
	 * @return record JSON format of an object
	 */
	public String getFirst() {
		String record = null;
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			Document document = this.collection.find().first();
			document.remove("_id");
			record = document.toJson();
		}
		return record;
	}
	
	/**
	 * This method gets only the first record of the collection, given a specific query.
	 * @param queryTuple (variable, value) of the record/records we want to be returned
	 * @return record JSON format of an object
	 */
	public String getFirst(BasicDBObject query) {
		String record = null;
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			Document document = this.collection.find(query).first();	
			document.remove("_id");
			record = document.toJson();
		}
		return record;
	}
	
	/**
	 * This method updates a record, given a specific query and changes we want to be made.
	 * @param queryTuple (variable, value) of the record we want to be changed
	 * @param updateRecordTuples (variable, value) of the new variables that will replace the old ones
	 * @return acknowledgment, the result of updating a record: success or failure
	 */
	public boolean update(BasicDBObject query, BasicDBObject updateRecord) {
		try (MongoClient mongoClient = MongoClients.create(this.uri)) {
			this.database = mongoClient.getDatabase("tg-db");
			this.collection = this.database.getCollection(this.collectionName);
			
			BasicDBObject updateObject = new BasicDBObject();
			updateObject.put("$set", updateRecord);
			UpdateResult updateResult = this.collection.updateOne(query, updateObject);
			return updateResult.wasAcknowledged();
		}
	}
	
	public static void main(String[] args) {
		// Testing: create a mongoDB object for the events
		//MongoDB<Event> mongo = new MongoDB<Event>(Event.class);
		
		//BasicDBObject bs = new BasicDBObject("test","This is a test");
		//Event event = new Event("e1","aggelos");
		//Gson gson = new Gson();
		//System.out.println(gson.toJson(event));
		//System.out.println(gson.fromJson(gson.toJson(event), Event.class).getCreator());
		
		// Testing: getAll
		//ArrayList<String> array = mongo.getAll();
		//for (String str:array) {
		//	System.out.println(str);
		//}
		
		// Testing: delete
		//BasicDBObject query = new BasicDBObject("eventID", "e1");
		//System.out.println(mongo.getFirst(query));
		//mongo.delete(query);
		
		// Testing: update
		//BasicDBObject query = new BasicDBObject("eventID", "e3");
		//BasicDBObject update = new BasicDBObject("eventID", "e2");
		//mongo.update(query, update);
		
		// Testing: update - given an Event object
		//Event event = new Event("e1","aggelos");
		//BasicDBObject query = new BasicDBObject("eventID", "e2");
		//String jsonf = gson.toJson(event);
		//BasicDBObject update = BasicDBObject.parse(jsonf);
		//mongo.update(query, update);
		
		//Event event = new Event("e101", "aggelos_dokimi");
		//String record = gson.toJson(event);
		//mongo.insert(gson.toJson(event));
	}
}
