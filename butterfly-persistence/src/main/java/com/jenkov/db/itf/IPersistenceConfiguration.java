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


import com.jenkov.db.impl.init.DatabaseInitializer;
import com.jenkov.db.scope.IScopeFactory;

import javax.sql.DataSource;

/**
 * This interface represents a persistence configuration which is a collection of Butterfly Persistence compononents
 * used to achieve persistence in a certain way. You can have different persistence configurations suiting
 * different databases or situations etc if helps you. The persistence configuration also makes it easier
 * to pass around all the different components to be used in a specific situation, for instance read-by-primary-key,
 * or inser / updateBatch / delete etc.
 *
 * @author Jakob Jenkov,  Jenkov Development
 */
public interface IPersistenceConfiguration {



    /**
     * Gets the data source associated with this persistence configuration.
     * @return The data source associated with this persistence configuration.
     */
    public DataSource getDataSource();

    /**
     * Sets the data source associated with this persistence configuration.
     */
    public void setDataSource(DataSource dataSource);

    /**
     * Returns the scope factory matching the data source set on this persistence configuration.
     * A scope factory is automatically created when a DataSource is set on an IPersistenceConfiguration. 
     * @return The scope factory matching the data source set on this persistence configuration.
     */
    public IScopeFactory getScopeFactory();

    /**
     * Returns the key by which this persistence configuration is stored internally
     * in the MrPersister class.
     * @return The key by which this persistence configuration is stored internally
     * in the MrPersister class.
     */
    public Object getConfigurationKey();

    /**
     * Sets the key by which this persistence configuration is stored internally
     * in the MrPersister class. Note: Changing the key in the
     * <code>IPersistenceConfiguration</code> instance will not
     * remap the instance stored in the MrPersister class. You will have to
     * remove the previously stored <code>IPersistenceConfiguration</code>
     * yourself.
     *
     * <br/><br/>
     * Calling the updateBatch method of a <code>IPersistenceConfiguration</code>
     * instance will however store that instance by the new key. But the instance will
     * remain mapped to the old key as well in the MrPersister class.
     * @param key key by which this persistence configuration is stored internally
     * in the MrPersister class, until you specifically remove that instance
     * from the MrPersister class.
     */
    public void   setConfigurationKey(Object key);


    /**
     * Returns the DatabaseInitializer used in this configuration.
     * @return The DatabaseInitializer used in this configuration.
     */
    public DatabaseInitializer getDatabaseInitializer();


    /**
     * Sets the DatabaseInitializer to use in this configuration.
     * @param initializer The DatabaseInitializer to use in this configuration.
     */
    public void setDatabaseInitializer(DatabaseInitializer initializer);


}
