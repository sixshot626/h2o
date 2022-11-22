package h2o.dao;


import java.sql.Connection;

public interface Db {

    String getName();

    void setQueryTimeout(Integer queryTimeout);

    void setUpdateTimeout(Integer updateTimeout);

    Dao getDao();

    Dao createDao(Connection connection);

}
