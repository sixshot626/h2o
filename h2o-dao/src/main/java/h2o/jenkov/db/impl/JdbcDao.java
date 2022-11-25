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

import h2o.jenkov.db.itf.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class JdbcDao implements IJdbcDao {

    protected IDaos daos = null;

    public JdbcDao(IDaos daos) {
        this.daos = daos;
    }



    public Object read(String sql, IPreparedStatementManager statementManager, IResultSetProcessor processor) throws PersistenceException {
        PreparedStatement statement = null;
        ResultSet         result    = null;
        SQLException      exception = null;
        try {
            if(statementManager instanceof PreparedStatementManagerBase){
                ((PreparedStatementManagerBase) statementManager).setIsQuery(true);
            }

            statement = statementManager.prepare(sql, daos.getConnection());
            if(statement == null) {
                statement = daos.getConnection().prepareStatement(sql);
            }
            statementManager.init(statement);

            result = (ResultSet) statementManager.execute(statement);

            if ( processor.init(result, this.daos) ) {

                while (result.next()) {
                    if ( ! processor.process(result, this.daos) ) {
                        break;
                    }
                }

            }

            statementManager.postProcess(statement);

            return processor.getResult();

        } catch(SQLException e){

            exception = e;
            return null; // won't happen. exception will be rethrown from finally clause.
        }
        finally {
            SQLException resultSetCloseException = null;
            if(result != null){
                try {
                    result.close();
                } catch (SQLException e) {
                    resultSetCloseException = e;
                }
            }

            //todo improve exception handling here?
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    if (exception               != null) throw new PersistenceException("Error during read operation",exception);
                    if (resultSetCloseException != null) throw new PersistenceException("Error closing ResultSet", resultSetCloseException);
                    throw new PersistenceException("Error closing PreparedStatement", e);
                }
            }
            if(exception != null) throw new PersistenceException("Error during read operation",exception);
            if(resultSetCloseException != null) throw new PersistenceException("Error closing ResultSet", resultSetCloseException);
        }
    }



    public int update(String sql, IPreparedStatementManager statementManager) throws PersistenceException{
        PreparedStatement statement = null;

        PersistenceException persistenceException = null;
        SQLException         sqlException         = null;
        try {
            if(statementManager instanceof PreparedStatementManagerBase){
                ((PreparedStatementManagerBase) statementManager).setIsQuery(false);
            }
            statement = statementManager.prepare(sql, daos.getConnection());
            statementManager.init(statement);
            int affectedRows = ((Integer) statementManager.execute(statement)).intValue();
            statementManager.postProcess(statement);
            return affectedRows;
        } catch (SQLException e) {
            sqlException = e;
            return 0; //never happens, exception is rethrown in finally clause
        } catch (PersistenceException e) {
            persistenceException = e;
            return 0; //never happens, exception is rethrown in finally clause
        } finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    if(sqlException != null) throw new PersistenceException("Error executing update", sqlException);
                    if(persistenceException != null) throw persistenceException;
                    throw new PersistenceException("Error closing PreparedStatement", e);
                }
            }
            if(sqlException != null)throw new PersistenceException("Error executing update", sqlException);
            if(persistenceException != null) throw persistenceException;
        }
    }
}
