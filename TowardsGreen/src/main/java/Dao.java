import java.util.ArrayList;

public interface Dao<T> {
	public ArrayList<T> getAll();
	public ArrayList<T> getAll(String id);
	public T getFirst();
	public T getFirst(String id);
	public void insert(T obj);
	public void update(String id, T updatedObj);
	public void delete(String id);
}
