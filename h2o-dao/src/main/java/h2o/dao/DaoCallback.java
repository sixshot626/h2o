package h2o.dao;

public interface DaoCallback<T> {

	T doCallback( Dao dao , Object scopeObj ) throws Exception;

}
