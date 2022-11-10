package h2o.dao.jdbc;


import com.jenkov.db.impl.Daos;
import com.jenkov.db.itf.*;
import h2o.common.Mode;
import h2o.common.lang.SNumber;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.exception.DaoException;
import h2o.dao.jdbc.sqlpara.PreparedSqlAndParameters;
import h2o.dao.jdbc.sqlpara.SqlParameterUtil;
import h2o.dao.log.LogWriter;
import h2o.dao.proc.RowProcessor;
import h2o.dao.result.RowData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@SuppressWarnings({"rawtypes", "unchecked"})
public class JdbcDao implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(JdbcDao.class.getName());

    private static boolean SHOWSQL = Mode.isUserMode("SHOW_JDBC_SQL");


    private LogWriter logWriter;

    private final IDaos daos;
    private final IJdbcDao jdbcDao;
    private final IMapDao mapDao;

    private boolean autoClose = true;


    public JdbcDao(Connection connection) {
        this.daos = new Daos(connection);
        this.jdbcDao = daos.getJdbcDao();
        this.mapDao = daos.getMapDao();
    }


    public void setAutoClose(boolean autoClose) {
        this.autoClose = autoClose;
    }

    public void setLogWriter(LogWriter logWriter) {
        this.logWriter = logWriter;
    }




    public <T> T read(String sql, SNumber fetchSize, IResultSetProcessor processor, Object... parameters) {

        if (SHOWSQL) {
            log.info("read(sql,IResultSetProcessor,Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("read", sql, Arrays.asList(parameters));
        }

        try {
            return (T) jdbcDao.read(sql,  new BasePreparedStatementManager(fetchSize,parameters) , processor);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }


    public <T> T read(String sql, SNumber fetchSize, IResultSetProcessor processor, Map paramMap) {

        if (SHOWSQL) {
            log.info("read(sql,IResultSetProcessor,Map):{}, para:{}", sql, paramMap);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("read", sql, paramMap);
        }

        PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);

        return read(sqlAndPara.sql, fetchSize, processor, sqlAndPara.paras);

    }

    public <T> T read(String sql, IPreparedStatementManager statementManager, IResultSetProcessor processor) {

        if (SHOWSQL) {
            log.info("read(sql,IPreparedStatementManager,IResultSetProcessor):{}", sql);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("read", sql, null);
        }

        try {
            return (T) jdbcDao.read(sql, (IPreparedStatementManager)statementManager, processor);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }

    public int update(String sql) {

        if (SHOWSQL) {
            log.info("update(sql):{}", sql);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("update", sql, null);
        }

        try {
            return jdbcDao.update(sql);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }

    public int update(String sql, Object... parameters) {

        if (SHOWSQL) {
            log.info("update(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("update", sql, Arrays.asList(parameters));
        }

        try {
            return jdbcDao.update(sql, parameters);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }


    public int update(String sql, Map paramMap) {

        if (SHOWSQL) {
            log.info("update(sql, Map):{}, para:{}", sql, paramMap);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("update", sql, paramMap);
        }

        PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);

        return update(sqlAndPara.sql, sqlAndPara.paras);
    }

    public int update(String sql, IPreparedStatementManager statementManager) {

        if (SHOWSQL) {
            log.info("update(sql, IPreparedStatementManager):{}", sql);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("update", sql, null);
        }

        try {
            return jdbcDao.update(sql, (IPreparedStatementManager)statementManager);
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }


    //=====================================================//
    // mapDao


    private volatile RowProcessor rowDataProcessor;

    public JdbcDao setRowDataProcessor(RowProcessor rowDataProcessor) {
        this.rowDataProcessor = rowDataProcessor;
        return this;
    }

    private Map<String, Object> dataProc(Map<String, Object> m) {

        RowProcessor rdp = rowDataProcessor;
        if (rdp != null) {
            return rdp.proc(m);
        } else {
            return m == null ? null : new RowData(m);
        }

    }

    private List<Map<String, Object>> listProc(List<Map<String, Object>> ml) {

        List<Map<String, Object>> nl = ListBuilder.newList();
        for (Map<String, Object> m : ml) {
            nl.add(dataProc(m));
        }

        return nl;
    }


    public <T> T readObject(String sql, Object... parameters) {

        if (SHOWSQL) {
            log.info("readObject(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readObject", sql, Arrays.asList(parameters));
        }

        try {

            Map data = mapDao.readMap(sql, (IPreparedStatementManager)new BasePreparedStatementManager( SNumber.ONE , parameters ));
            return CollectionUtil.isEmpty(data) ? null : (T) data.values().iterator().next();

        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }
    }


    public <T> T readObject(String sql, Map paramMap) {

        if (SHOWSQL) {
            log.info("readObject(sql, Map):{}, para:{}", sql, paramMap);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readObject", sql, paramMap);
        }

        PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);

        return readObject(sqlAndPara.sql, sqlAndPara.paras);
    }




    public Map<String, Object> readMap(String sql, Object... parameters) {

        if (SHOWSQL) {
            log.info("readMap(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMap", sql, Arrays.asList(parameters));
        }

        try {
            return dataProc(mapDao.readMap(sql, (IPreparedStatementManager)new BasePreparedStatementManager(SNumber.ONE, parameters)));
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }

    public Map<String, Object> readMap(String sql, Map paramMap) {

        if (SHOWSQL) {
            log.info("readMap(sql, Map):{}, para:{}", sql, paramMap);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMap", sql, paramMap);
        }

        PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);

        return readMap(sqlAndPara.sql, sqlAndPara.paras);

    }


    public Map<String, Object> readMap(String sql, IPreparedStatementManager statementManager) {

        if (SHOWSQL) {
            log.info("readMap(sql, IPreparedStatementManager):{}", sql);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMap", sql, null);
        }

        try {
            return dataProc(mapDao.readMap(sql, (IPreparedStatementManager)statementManager));
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }



    public List<Map<String, Object>> readMapList(String sql, SNumber fetchSize, Object... parameters) {

        if (SHOWSQL) {
            log.info("readMapList(sql, Object...):{}, para:{}", sql, Arrays.asList(parameters));
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMapList", sql, Arrays.asList(parameters));
        }

        try {
            return listProc(mapDao.readMapList(sql, (IPreparedStatementManager)new BasePreparedStatementManager(fetchSize, parameters)));
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }

    public List<Map<String, Object>> readMapList(String sql, SNumber fetchSize, Map paramMap) {

        if (SHOWSQL) {
            log.info("readMapList(sql, Map):{}, para:{}", sql, paramMap);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMapList", sql, paramMap);
        }

        PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, paramMap);

        return readMapList(sqlAndPara.sql, fetchSize, sqlAndPara.paras);

    }


    public List<Map<String, Object>> readMapList(String sql, IPreparedStatementManager statementManager) {

        if (SHOWSQL) {
            log.info("readMapList(sql, IPreparedStatementManager):{}", sql);
        }
        if (logWriter != null && logWriter.isOn()) {
            logWriter.write("readMapList", sql, null);
        }

        try {
            return listProc(mapDao.readMapList(sql, (IPreparedStatementManager)statementManager));
        } catch (PersistenceException e) {
            throw new DaoException(e);
        } finally {
            autoCloseConnection();
        }

    }


    private PreparedSqlAndParameters toPreparedSqlAndPara(String sql, Map paramMap) {
        return SqlParameterUtil.toPreparedSqlAndPara(sql, paramMap);
    }


    @Override
    public void close() throws DaoException {
        try {
            this.daos.closeConnection();
        } catch (PersistenceException e) {
            log.warn("closeConnection", e);
        }
    }

    private void autoCloseConnection() {
        if (this.autoClose) {
            this.close();
        }
    }


}
