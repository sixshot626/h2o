package h2o.dao.transaction;


import h2o.dao.Dao;

public interface TransactionManager {

    void rollback();

    void commit();

    Dao getDao();

}
