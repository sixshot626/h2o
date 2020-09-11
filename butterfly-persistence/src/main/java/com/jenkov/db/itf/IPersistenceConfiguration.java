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



}
