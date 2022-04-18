import java.util.ArrayList;

public interface Dao<T> {
	public ArrayList<T> getAll();
	public T get(String id);
	public void insert(T obj);
	public void update(String id);
	public void delete(String id);
}
