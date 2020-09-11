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

import com.jenkov.db.itf.IPersistenceConfiguration;

import javax.sql.DataSource;

/**
 * This class is an implementation of the <code>IPersistenceConfiguration</code> interface.
 * All the JavaDoc is included in that interface.
 */

public class PersistenceConfiguration implements IPersistenceConfiguration{

    private final DataSource          dataSource;

    public PersistenceConfiguration( DataSource dataSource ) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

}
