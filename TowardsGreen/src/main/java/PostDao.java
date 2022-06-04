import com.mongodb.BasicDBObject;

import java.util.ArrayList;

public class PostDao implements Dao{

    private static PostDao postDao = null;
    private MongoDB mongoDB;

    private PostDao(){
        this.mongoDB= new MongoDB("Post");
    }

    public static PostDao getInstance(){
        if(postDao == null){
            postDao = new PostDao();
        }
        return postDao;
    }

    @Override
    public ArrayList<String> getAll() {
        return this.mongoDB.getAll();
    }

    @Override
    public ArrayList<String> getAll(String id) {
        BasicDBObject query = new BasicDBObject("postID", id);
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
        BasicDBObject query = new BasicDBObject("postID", id);
        String record = this.mongoDB.getFirst(query);
        return record;
    }

    @Override
    public boolean insert(String post) {
        return this.mongoDB.insert(post);
    }
    //TODO: fix
//    public boolean insert(Profile profile) {
//        return this.mongoDB.insert(profile);
//    }

    @Override
    public boolean update(String id, String updatedPost) {
        BasicDBObject query = new BasicDBObject("postID", id);
        BasicDBObject updateRecord = BasicDBObject.parse(updatedPost);
        return this.mongoDB.update(query, updateRecord);

    }

    @Override
    public boolean delete(String id) {
        BasicDBObject query = new BasicDBObject("postID", id);
        return this.mongoDB.delete(query);
    }
}
