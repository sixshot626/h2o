package h2o.dao.result;

import h2o.dao.Dao;

import java.sql.ResultSet;


public interface ResultSetCallback<T> {

    boolean init(ResultSet rs, Dao dao) throws Exception;

    boolean process(ResultSet rs, Dao dao) throws Exception;

    T getResult() throws Exception;


}
