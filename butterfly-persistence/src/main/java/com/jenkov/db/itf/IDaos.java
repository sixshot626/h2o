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



package com.jenkov.db.itf;

import java.sql.Connection;

/**
 * todo JavaDoc this interface.
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public interface IDaos {

    /**
     * Returns the connection used by this IDaos instance, and by all the DAO's returned by this IDaos instance.
     * @return The connection used by this IDaos instance, and by all the DAO's returned by this IDaos instance.
     */
    public Connection                getConnection();

    /**
     * Returns the persistence configuration used by the PersistenceManager where this IDaos came from.
     * Some of the DAO's returned by this IDaos instance also use this persistence configuration.
     * Most of the time you will not need to access the persistence configuration.
     *
     * @return The persistence configuration used by the PersistenceManager where this IDaos came from.
     */
    public IPersistenceConfiguration getConfiguration();



    /**
     * Returns an IJdbcDao instance. If this method is called more than once, the same IJdbcDao
     * instance is returned.
     *
     * @return An IJdbcDao instance.
     */
    public IJdbcDao                  getJdbcDao();

    /**
     * Returns an IMapDao instance. If this method is called more than once, the same IMapDao
     * instance is returned.
     *
     * @return An IMapDao instance.
     */
    public IMapDao                   getMapDao();


    /**
     * Closes the connection used by this IDaos instance. If closing the connection fails
     * a PersistenceException is thrown (this exception is unchecked).
     */
    public void                      closeConnection() throws PersistenceException;

}
