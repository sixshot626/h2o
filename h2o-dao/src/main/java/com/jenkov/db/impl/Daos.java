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

import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.IJdbcDao;
import com.jenkov.db.itf.IMapDao;
import com.jenkov.db.itf.PersistenceException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class Daos implements IDaos {

    private final Connection                connection;
    private final IJdbcDao                  jdbcDao;
    private final IMapDao                   mapDao;


    public Daos(Connection connection) {
        this.connection = connection;
        this.jdbcDao = new JdbcDao(this);
        this.mapDao = new MapDao(this);
    }

    public Connection getConnection() {
        return connection;
    }

    public IJdbcDao getJdbcDao() {
        return jdbcDao;
    }

    public IMapDao getMapDao() {
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
