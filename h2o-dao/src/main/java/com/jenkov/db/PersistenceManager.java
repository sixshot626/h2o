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



package com.jenkov.db;

import com.jenkov.db.impl.Daos;
import com.jenkov.db.itf.IDaos;
import com.jenkov.db.itf.PersistenceException;
import com.jenkov.db.scope.ScopingDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * This class is the starting point of all Butterfly Persistence use. Many things are cached
 * inside the PersistenceManager, so you should reuse the same instance once you have created it. Assign it to a
 * constant, or make it a singleton, like this:
 *
 * <br/><br/>
 * <code>
 * public static final PERSISTENCE_MANAGER = new PersistenceManager();
 *
 * <br/><br/>
 * IDaos       daos = PERSISTENCE_MANAGER.createDaos(connection);
 * </code>
 *
 *
 * <br/><br/>
 * The PersistenceManager instance
 * should be reused throughout the application lifetime. Each application should create it's own PersistenceManager
 * instance.
 *
 * <br/><br/>
 * It is safe to share PersistenceManager instances if: <br/><br/>
 * 1) The components sharing them are reading the same type of objects from the same database.<br/>
 * 2) The components sharing them are reading different types of objects from the same or different databases.<br/>
 *
 *
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class PersistenceManager {

    private final DataSource dataSource;


    public PersistenceManager(DataSource dataSource){
        this.dataSource = dataSource;
    }



    public ScopingDataSource getScopingDataSource(){
        if(!(getDataSource() instanceof ScopingDataSource))
            throw new IllegalStateException("The DataSource set on the PersistenceManager is not a ScopingDataSource");

        return (ScopingDataSource) getDataSource();        
    }

    /**
     * @return the <code>DataSource</code> used by this <code>PersistenceManager</code> .
     */
    public DataSource getDataSource(){
        return this.dataSource;
    }


    protected Connection getConnection() throws SQLException {
        return this.getDataSource().getConnection();
    }


    /** Creates an IDaos instance containing a connection obtained from the DataSource set on
     *         the configuration of this PersistenceManager.
     * @return An IDaos instance containing a connection obtained from the DataSource set on
     *         the configuration of this PersistenceManager.
     * @throws PersistenceException If opening the connection fails.
     */
    public IDaos createDaos() throws PersistenceException {
        try {
            return createDaos(this.getConnection());
        } catch (SQLException e) {
            throw new PersistenceException("Error creating IDaos instance", e);
        }
    }

    /** Creates an IDaos instance containing the connection passed as parameter.
     * @param  connection The Connection to use in the IDaos instance.
     * @return An IDaos instance containing the connection passed as parameter.
     */
    public IDaos createDaos(Connection connection){
        return new Daos(connection);
    }



}
