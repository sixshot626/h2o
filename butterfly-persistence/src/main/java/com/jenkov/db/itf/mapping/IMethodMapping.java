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



/**
 * User: Administrator
 */
package com.jenkov.db.itf.mapping;

import java.lang.reflect.Method;

/**
 * This interface represents the functions that both IGetterMapping and
 * ISetterMapping instances have in common (it is the super-interface
 * of both).
 *
 * <br/><br/>
 * A method method maps a method in a class (getter or setter) to a column
 * name in a table in the database.
 *
 * <br/><br/>
 * Field mappings are stored in the object mappings and used by the object reader
 * and object writer to determine what columns from the database to read and write.
 * They are also used by the SQL generator to generate the SQL to read and write
 * the objects.
 *
 * @author Jakob Jenkov, Jenkov Development
 */

public interface IMethodMapping {

    /**
     * Returns the database table column name this method method is method to.
     * @return The database table column name this method method is method to.
     */
    public String getColumnName();

    /**
     * Sets the database column name this method method is method to.
     * @param fieldName The column name to map to.
     */
    public void   setColumnName(String fieldName);


    /**
     * Returns the column type as defined in the database. The int returned will match one
     * of the java.sql.Types.XXX constants.
     * @return The column type as defined in the database.
     */
    public int getColumnType();


    /**
     * Sets the column type as defined in the database. The int should match one
     * of the java.sql.Types.XXX constants.
     * @param columnType The column type as defined in the database.
     */
    public void setColumnType(int columnType);


    /**
     * Returns the method instance this method method is method from.
     * @return The <code>Method</code> instance this method method is method from.
     */
    public Method  getObjectMethod();

    /**
     * The method instance this method method maps from.
     * @param member The <code>Method</code> instance this method method maps from.
     */
    public void    setObjectMethod(Method member);


    /**
     * Returns true if the database column referenced by this method method exists in a table in the database.
     * False if not. Objects can be mapped to SQL queries with arbitrary column names not existing in the database.
     * For instance a setter method of an object can be mapped to the column name totalCount in the SQL query
     * <code>"select count(*) totalCount from employees</code>
     * @return  True if the database column referenced by this method method exists in a table in the database.
     * False if not.
     */
    public boolean isTableMapped();

    /**
     * Sets whether or not the database column referenced by this method method
     * exists in a table in the database. Set to true if it does. False if not.
     * Objects can be mapped to SQL queries with arbitrary column names not existing in the database.
     * For instance a setter method of an object can be mapped to the column name totalCount in the SQL query
     * <code>"select count(*) totalCount from employees</code>
     * @param isTableMapped
     */
    public void    setTableMapped(boolean isTableMapped);
}
