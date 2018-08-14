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
import com.jenkov.db.impl.init.DatabaseInitializer;
import com.jenkov.db.itf.IPersistenceConfiguration;
import com.jenkov.db.scope.IScopeFactory;
import com.jenkov.db.scope.ScopeFactory;

import javax.sql.DataSource;

/**
 * This class is an implementation of the <code>IPersistenceConfiguration</code> interface.
 * All the JavaDoc is included in that interface.
 */

public class PersistenceConfiguration implements IPersistenceConfiguration{

    protected PersistenceManager  persistenceManager       = null;
    protected Object              configurationKey         = null;



    protected DataSource          dataSource               = null;

    protected DatabaseInitializer databaseInitializer      = new DatabaseInitializer();
    protected IScopeFactory       scopeFactory             = null;



    public PersistenceConfiguration( PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public synchronized DataSource getDataSource() {
        return dataSource;
    }

    public synchronized void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        setScopeFactory(new ScopeFactory(dataSource));
    }

    public IScopeFactory getScopeFactory() {
        return scopeFactory;
    }

    public void setScopeFactory(IScopeFactory scopeFactory) {
        this.scopeFactory = scopeFactory;
    }

    public synchronized Object getConfigurationKey() {
        return configurationKey;
    }

    public synchronized void setConfigurationKey(Object configurationKey) {
        this.configurationKey = configurationKey;
    }

    public synchronized PersistenceManager getPersistenceManager() {
        return persistenceManager;
    }

    public DatabaseInitializer getDatabaseInitializer() {
        return databaseInitializer;
    }

    public void setDatabaseInitializer(DatabaseInitializer databaseInitializer) {
        this.databaseInitializer = databaseInitializer;
    }
}
