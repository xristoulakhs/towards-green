//import java.util.ArrayList;
//
//public interface Dao<T> {
//	public ArrayList<T> getAll();
//	public ArrayList<T> getAll(String id);
//	public T getFirst();
//	public T getFirst(String id);
//	public void insert(T obj);
//	public void update(String id, T updatedObj);
//	public void delete(String id);
//}


// Version 2
import java.util.ArrayList;

public interface Dao {
	public ArrayList<String> getAll();
	public ArrayList<String> getAll(String id);
	public ArrayList<String> getFirstN(int limit);
	public ArrayList<String> getFirstN(int limit, int skip);
	public String getFirst();
	public String getFirst(String id);
	public boolean insert(String obj);
	public boolean update(String id, String updatedObj);
	public boolean delete(String id);
}
