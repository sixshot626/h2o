package h2o.dao.impl;

import h2o.dao.Dao;
import h2o.dao.Db;
import h2o.dao.DbUtil;
import h2o.dao.connection.ConnectionManager;
import h2o.dao.page.PagingProcessor;

import java.sql.Connection;
import java.util.Optional;


public class DbImpl implements Db {


    private final String name;

    private final ConnectionManager connectionManager;


    private Integer queryTimeout;

    private Integer updateTimeout;

    public DbImpl(String name, ConnectionManager connectionManager) {
        this.name = name;
        this.connectionManager = connectionManager;
    }




    @Override
    public String getName() {
        return name;
    }


    @Override
    public void setQueryTimeout(Integer queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    @Override
    public void setUpdateTimeout(Integer updateTimeout) {
        this.updateTimeout = updateTimeout;
    }

    @Override
    public Dao getDao() {
        ConnectionManager cm = this.connectionManager;
        return createDao(cm.getConnection());
    }

    @Override
    public Dao createDao(Connection connection) {

        DaoImpl daoImpl = new DaoImpl(connection);
        daoImpl.setQueryTimeout(this.queryTimeout);
        daoImpl.setUpdateTimeout(this.updateTimeout);

        daoImpl.setArgProcessor(DbUtil.DBFACTORY.getArgProcessor());
        daoImpl.setRowProcessor(DbUtil.DBFACTORY.getRowProcessor());
        daoImpl.setOrmProcessor(DbUtil.DBFACTORY.getOrmProcessor());

        Optional<PagingProcessor> pagingProcessor = DbUtil.DBFACTORY.getPagingProcessor(this.getName());
        if (pagingProcessor.isPresent()) {
            daoImpl.setPagingProcessor(pagingProcessor.get());
        }

        return daoImpl;
    }

}
