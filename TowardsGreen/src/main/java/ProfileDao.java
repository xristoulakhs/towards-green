import com.mongodb.BasicDBObject;

import java.util.ArrayList;

public class ProfileDao implements Dao{

    private static ProfileDao profileDao = null;
    private MongoDB mongoDB;

    private ProfileDao(){
        this.mongoDB= new MongoDB("Profile");
    }

    public static ProfileDao getInstance(){
        if(profileDao == null){
            profileDao = new ProfileDao();
        }
        return profileDao;
    }

    @Override
    public ArrayList<String> getAll() {
        return this.mongoDB.getAll();
    }

    @Override
    public ArrayList<String> getAll(String id) {
        BasicDBObject query = new BasicDBObject("userID", id);
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
        BasicDBObject query = new BasicDBObject("userID", id);
        String record = this.mongoDB.getFirst(query);
        return record;
    }
    
    public String getFirstWithEmail(String id) {
        BasicDBObject query = new BasicDBObject("email", id);
        String record = this.mongoDB.getFirst(query);
        return record;
    }

    @Override
    public boolean insert(String profile) {
        return this.mongoDB.insert(profile);
    }

    @Override
    public boolean update(String id, String updatedProfile) {
        BasicDBObject query = new BasicDBObject("userID", id);
        BasicDBObject updateRecord = BasicDBObject.parse(updatedProfile);
        return this.mongoDB.update(query, updateRecord);

    }

    @Override
    public boolean delete(String id) {
        BasicDBObject query = new BasicDBObject("userID", id);
        return this.mongoDB.delete(query);
    }
}
