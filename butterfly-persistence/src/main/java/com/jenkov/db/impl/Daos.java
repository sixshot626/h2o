/*
    Copyright 2008 Jenkov Development

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/



package com.jenkov.db.impl;

import com.jenkov.db.PersistenceManager;
import com.jenkov.db.itf.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class Daos implements IDaos {

    protected PersistenceManager persistenceManager   = null;
    protected Connection                connection    = null;
    protected IPersistenceConfiguration configuration = null;
    protected IJdbcDao                  jdbcDao       = null;
    protected IMapDao                   mapDao        = null;


    public Daos(Connection connection, IPersistenceConfiguration configuration, PersistenceManager manager) {
        this.connection = connection;
        this.configuration = configuration;
        this.persistenceManager = manager;
    }

    public Connection getConnection() {
        return connection;
    }

    public IPersistenceConfiguration getConfiguration() {
        return configuration;
    }



    public synchronized IJdbcDao getJdbcDao() {
        if(this.jdbcDao == null){
            this.jdbcDao = new JdbcDao(this);
        }
        return jdbcDao;
    }

    public synchronized IMapDao getMapDao() {
        if(this.mapDao == null){
            this.mapDao = new MapDao(this);
        }
        return this.mapDao;
    }



    public void closeConnection() throws PersistenceException {
        if(this.connection != null){
            try {
                this.connection.close();
            } catch (SQLException e) {
                throw new PersistenceException("Error closing connection", e);
            }
        }
    }
}
