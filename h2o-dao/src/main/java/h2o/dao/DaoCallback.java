package h2o.dao;

public interface DaoCallback<T> {

	default T doCallback( Dao dao , Object scopeObj ) throws Exception {
		return this.doCallback( dao );
	}
	
	T doCallback( Dao dao ) throws Exception;

}
