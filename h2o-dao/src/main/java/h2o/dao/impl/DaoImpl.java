package h2o.dao.impl;

import h2o.jenkov.db.impl.ResultSetProcessorBase;
import h2o.jenkov.db.itf.IDaos;
import h2o.jenkov.db.itf.IResultSetProcessor;
import h2o.jenkov.db.itf.PersistenceException;
import h2o.common.Mode;
import h2o.common.exception.ExceptionUtil;
import h2o.common.lang.SNumber;
import h2o.common.lang.Val;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.Dao;
import h2o.dao.ResultSetCallback;
import h2o.dao.exception.DaoException;
import h2o.dao.jdbc.BatchPreparedStatementManager;
import h2o.dao.jdbc.JdbcDao;
import h2o.dao.jdbc.sqlpara.PreparedSqlAndParameters;
import h2o.dao.jdbc.sqlpara.SqlParameterUtil;
import h2o.dao.log.LogWriter;
import h2o.dao.sql.SqlSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;


public class DaoImpl extends AbstractDao implements Dao {

    private static final Logger log = LoggerFactory.getLogger(Dao.class.getName());

    private static boolean SHOW_SQL = !Mode.isUserMode("DONT_SHOW_SQL");


    private final JdbcDao jdbcDao;

    public DaoImpl(Connection connection) {
        super(connection);
        this.jdbcDao = new JdbcDao(connection);
        this.jdbcDao.setAutoClose(false);
    }


    @Override
    public void setQueryTimeout(Integer queryTimeout) {
        if (queryTimeout == null || queryTimeout <= 0 ) {
            this.jdbcDao.setQueryTimeout(SNumber.NULL);
        } else {
            this.jdbcDao.setQueryTimeout( new SNumber(queryTimeout) );
        }
    }

    @Override
    public void setUpdateTimeout(Integer updateTimeout) {
        if (updateTimeout == null || updateTimeout <= 0 ) {
            this.jdbcDao.setUpdateTimeout(SNumber.NULL);
        } else {
            this.jdbcDao.setUpdateTimeout( new SNumber(updateTimeout) );
        }
    }

    @Override
    public List<String> columnLabels(SqlSource sqlSource, Object... args) throws DaoException {
        try {

            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            if (SHOW_SQL) {
                log.info("SQL:columnLabels#\r\n{}\r\nPARA:{}\r\n", sql, paramMap);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("columnLabels", sql, paramMap);
                }
            }

            return jdbcDao.read( sql , SNumber.ZERO , new ResultSetProcessorBase() {

                public boolean init(ResultSet result, IDaos daos) throws SQLException, PersistenceException {

                    ResultSetMetaData metaData = result.getMetaData();

                    List<String> labels = new ArrayList<>( metaData.getColumnCount() );
                    for(int i=1, n=metaData.getColumnCount(); i<=n; i++){
                        labels.add(metaData.getColumnLabel(i));
                    }

                    this.setResult( labels );

                    return false;
                }


            } , paramMap );


        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }



    @Override
    public Val<Map<String, Object>> get(SqlSource sqlSource, Object... args) throws DaoException {

        try {

            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            if (SHOW_SQL) {
                log.info("SQL:get#\r\n{}\r\nPARA:{}\r\n", sql, paramMap);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("get", sql, paramMap);
                }
            }

            Map<String, Object> row = jdbcDao.readMap(sql, paramMap);
            Map<String, Object> r = this.rowProc(row);

            return r == null ? Val.empty() : new Val<>(r);

        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }


    @Override
    public List<Map<String, Object>> load(SqlSource sqlSource, Object... args) throws DaoException {
        try {
            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            if (SHOW_SQL) {
                log.info("SQL:load#\r\n{}\r\nPARA:{}\r\n", sql, paramMap);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("load", sql, paramMap);
                }
            }

            List<Map<String, Object>> rows = jdbcDao.readMapList(sql,SNumber.NULL, paramMap);
            List<Map<String, Object>> rs = new ArrayList<>(rows.size());

            for ( Map<String, Object> row : rows ) {
                rs.add( this.rowProc(row) );
            }

            return rs;

        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T> Val<T> load(final ResultSetCallback<T> rsCallback, SqlSource sqlSource, Object... args) throws DaoException {

        try {
            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            if (SHOW_SQL) {
                log.info("SQL:load(ResultSetCallback)#\r\n{}\r\nPARA:{}\r\n", sql, paramMap);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("load(ResultSetCallback)", sql, paramMap);
                }
            }

            T r = (T) jdbcDao.read(sql, SNumber.NULL, new IResultSetProcessor() {

                @Override
                public boolean init(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

                    try {

                        return rsCallback.init(rs, DaoImpl.this);

                    } catch (SQLException e) {
                        throw e;
                    } catch (Exception e) {
                        throw ExceptionUtil.toRuntimeException(e);
                    }

                }

                @Override
                public boolean process(ResultSet rs, IDaos idao) throws SQLException, PersistenceException {

                    try {

                        return rsCallback.process(rs, DaoImpl.this);

                    } catch (SQLException e) {
                        throw e;
                    } catch (Exception e) {
                        throw ExceptionUtil.toRuntimeException(e);
                    }

                }

                @Override
                public Object getResult() throws PersistenceException {

                    try {

                        return rsCallback.getResult();

                    } catch (Exception e) {
                        throw ExceptionUtil.toRuntimeException(e);
                    }

                }

            }, paramMap);

            return r == null ? Val.empty() : new Val<>(r);

        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }


    @Override
    public int update(SqlSource sqlSource, Object... args) throws DaoException {
        try {
            Map<String, Object> paramMap = this.argProc(args);
            String sql = sqlSource.getSql(paramMap);

            if (SHOW_SQL) {
                log.info("SQL:update#\r\n{}\r\nPARA:{}\r\n", sql, paramMap);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("update", sql, paramMap);
                }
            }

            return jdbcDao.update(sql, paramMap);

        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }


    @Override
    public int[] batchUpdate(SqlSource sqlSource, Collection<?> args) throws DaoException {
        try {



            List<Object[]> nArgs = ListBuilder.newList();
            String nSql = null;

            if (CollectionUtil.isEmpty(args)) {

                throw new IllegalArgumentException("Args is empty.");

            }
            for (Object arg : args) {

                Object[] aa;
                if (arg.getClass().isArray()) {
                    aa = (Object[]) arg;
                } else {
                    aa = new Object[]{arg};
                }

                Map<String, Object> paramMap = this.argProc(aa);

                String sql = sqlSource.getSql(paramMap);

                PreparedSqlAndParameters sqlAndPara = SqlParameterUtil.toPreparedSqlAndPara(sql, paramMap);
                nArgs.add(sqlAndPara.paras);

                if ( nSql == null ) {
                    nSql = sqlAndPara.sql;
                } else {
                    if ( !nSql.equals( sqlAndPara.sql ) ) {
                        throw new DaoException("Batch SQL is different");
                    }
                }

            }

            if (SHOW_SQL) {
                log.info("SQL:batchUpdate#\r\n{}\r\n", nSql);
            }
            {
                LogWriter writer = this.getLogWriter();
                if (writer != null && writer.isOn()) {
                    writer.write("batchUpdate", nSql, null);
                }
            }

            BatchPreparedStatementManager batchPreparedStatementManager = new BatchPreparedStatementManager(nArgs);

            jdbcDao.update(nSql, batchPreparedStatementManager);

            return batchPreparedStatementManager.getUpdateRows();

        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            autoCloseDao();
        }
    }


    @Override
    public void close() throws DaoException {
        try {
            this.jdbcDao.close();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }


    private void autoCloseDao() {
        if (this.isAutoClose()) {
            this.close();
        }
    }


}
