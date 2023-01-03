package h2o.dao.transaction;

import h2o.dao.Dao;

@FunctionalInterface
public interface TransactionCallback<T> {

	T doInTransaction( Dao dao ) throws Exception;

}