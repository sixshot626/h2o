package h2o.dao;

public interface DaoCallback<T> {
	
	T doCallBack(Dao dao) throws Exception ;

}
