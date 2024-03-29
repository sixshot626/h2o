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



package h2o.jenkov.db.impl;

import h2o.jenkov.db.itf.IPreparedStatementManager;
import h2o.jenkov.db.itf.PersistenceException;
import h2o.jenkov.db.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class PreparedStatementManagerBase implements IPreparedStatementManager {

    protected Object[] parameters = null;
    protected boolean isQuery = true;

    public PreparedStatementManagerBase() {
    }

    public PreparedStatementManagerBase(Object[] parameters) {
        this.parameters = parameters;
    }

    void setIsQuery(boolean isQuery){
        this.isQuery = isQuery;
    }

    public PreparedStatement prepare(String sql, Connection connection) throws SQLException, PersistenceException {
        return connection.prepareStatement(sql);
    }

    public void init(PreparedStatement statement)  throws SQLException, PersistenceException{
        if(parameters != null && parameters.length > 0){
            JdbcUtil.insertParameters(statement, parameters);
        }
    }

    public Object execute(PreparedStatement statement) throws SQLException, PersistenceException {
        if(this.isQuery){
            return statement.executeQuery();
        } else {
            return new Integer(statement.executeUpdate());
        }
    }

    public void postProcess(PreparedStatement statement)  throws SQLException, PersistenceException{

    }
}
