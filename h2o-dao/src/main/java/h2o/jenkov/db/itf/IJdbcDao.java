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



package h2o.jenkov.db.itf;

/**
 * Represents a DAO capable of simplifying the most ordinary JDBC tasks like reading
 * a long from the database, iterating a ResultSet and executing an update.
 *
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public interface IJdbcDao {



    /**
     * Executes the given SQL and calls the IResultSetProcessor's process(...) method
     * for each record in the ResultSet. Before iterating the ResultSet the IResultSetProcessor's
     * init(...) method is called. When the ResultSet is fully iterated the IResultSetProcessor's
     * getResult() method is called. The value returned from getResult() is the value returned
     * from this read(...) method.
     *
     * @param sql         The SQL that locates the records to iterate.
     * @param statementManager An instance capable of preparing, initializing
     *            parameters of, and post-processing the PreparedStatement
     *            being used to execute the SQL. It is easiest to extend
     *            the PreparedStatementManagerBase which has default implementations
     *            for the prepare(...), init(...), execute(...) and postProcess() methods.
     * @param processor   The IResultSetProcessor implementation that processes the ResultSet.
     *                    It is easiest to extend the ResultSetProcessorBase which has empty
     *                    implementations for init(...), process(...), and getResult(). Then
     *                    you only have to override the methods you need.
     * @return
     * @throws PersistenceException If anything goes wrong during the execution of the SQL and the
     *                    iteration of the ResultSet.
     */
    public Object read(String sql, IPreparedStatementManager statementManager, IResultSetProcessor processor) throws PersistenceException;



    /**
     * Executes the given SQL as an update (PreparedStatement.executeUpdate()).
     * Useful for insert, update and delete statements.
     *
     * @param sql The SQL containing the update.
     * @param statementManager An instance capable of preparing, initializing
     *            parameters of, and post-processing the PreparedStatement
     *            being used to execute the SQL. It is easiest to extend
     *            the PreparedStatementManagerBase which has default implementations
     *            for the prepare(...), init(...), execute(...) and postProcess() methods.
     * @return The number of records affected as returned by the PreparedStatement.executeUpdate() method.
     * @throws PersistenceException If anyting goes wrong during the update.
     */
    public int update(String sql, IPreparedStatementManager statementManager) throws PersistenceException;

}
