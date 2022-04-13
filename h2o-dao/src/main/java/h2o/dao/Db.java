package h2o.dao;


import java.sql.Connection;

public interface Db {

    String getName();

    Dao getDao();

    Dao createDao(Connection connection);

}
