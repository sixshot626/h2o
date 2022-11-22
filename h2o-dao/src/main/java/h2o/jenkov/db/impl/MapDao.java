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

import h2o.jenkov.db.itf.IDaos;
import h2o.jenkov.db.itf.IMapDao;
import h2o.jenkov.db.itf.IPreparedStatementManager;
import h2o.jenkov.db.itf.PersistenceException;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Jakob Jenkov - Copyright 2005 Jenkov Development
 */
public class MapDao implements IMapDao {

    protected IDaos daos = null;

    public MapDao(IDaos daos) {
        this.daos = daos;
    }

    public Map readMap(String sql, IPreparedStatementManager statementManager) throws PersistenceException {
        return (Map) this.daos.getJdbcDao().read(sql, statementManager, new ResultSetProcessorBase(){

            ResultSetMetaData metaData = null;

            public boolean init(ResultSet result, IDaos daos) throws SQLException, PersistenceException {
                this.metaData = result.getMetaData();
                return true;
            }

            public boolean process(ResultSet result, IDaos daos) throws SQLException, PersistenceException {
                setResult(readRecordIntoMap(result, this.metaData, (Map) getResult()));
                return false;
            }

        });
    }

    private Map readRecordIntoMap(ResultSet result, ResultSetMetaData metaData, Map map) throws SQLException {
        if(map == null) map = new LinkedHashMap();
        for(int i=1, n=metaData.getColumnCount(); i<=n; i++){
            map.put(metaData.getColumnLabel(i), result.getObject(i));
        }
        return map;
    }

    public List readMapList(String sql, IPreparedStatementManager statementManager) throws PersistenceException{
        return (List) this.daos.getJdbcDao().read(sql, statementManager,  new ResultSetProcessorBase(){
            ResultSetMetaData metaData = null;

            public boolean init(ResultSet result, IDaos daos) throws SQLException, PersistenceException {
                setResult(new ArrayList());
                this.metaData = result.getMetaData();
                return true;
            }

            public boolean process(ResultSet result, IDaos daos) throws SQLException, PersistenceException {
                ((List) getResult()).add(readRecordIntoMap(result, this.metaData, null));
                return true;
            }

        });
    }
}
