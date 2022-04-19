package h2o.dao.jdbc;

import com.jenkov.db.itf.IPreparedStatementManager;
import com.jenkov.db.itf.PersistenceException;
import com.jenkov.db.jdbc.JdbcUtil;
import h2o.common.Mode;
import h2o.dao.jdbc.sqlpara.PreparedSqlAndParameters;
import h2o.dao.jdbc.sqlpara.SqlParameterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;


public class BatchPreparedStatementManager implements IPreparedStatementManager {

    private static final Logger log = LoggerFactory.getLogger(BatchPreparedStatementManager.class.getName());

    private static boolean SHOWSQL = Mode.isUserMode("SHOW_BUTTERFLY_SQL");

    private final Collection<?> batch_parameters;

    private int[] updateRows;

    private String sql;


    public Collection<?> getBatch_parameters() {
        return batch_parameters;
    }

    public int[] getUpdateRows() {
        return updateRows;
    }

    public String getSql() {
        return sql;
    }


    public BatchPreparedStatementManager(Collection<?> batch_parameters) {
        this.batch_parameters = batch_parameters;
    }

    public BatchPreparedStatementManager(Object[][] batch_parameters) {
        this.batch_parameters = Arrays.asList(batch_parameters);
    }


    public PreparedStatement prepare(String sql, Connection connection) throws SQLException, PersistenceException {

        this.sql = sql;

        if (batch_parameters != null && !batch_parameters.isEmpty()) {

            Object parameters = batch_parameters.iterator().next();
            if (parameters instanceof Map) {

                @SuppressWarnings("unchecked")
                PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, (Map<String, Object>) parameters);

                sql = sqlAndPara.sql;

            }
        }

        if (SHOWSQL) {
            log.info("updateBatch--sql:{}", sql);
        }

        return connection.prepareStatement(sql);

    }


    @SuppressWarnings("rawtypes")
    public void init(PreparedStatement paramPreparedStatement) throws SQLException, PersistenceException {

        for (Object parameters : this.batch_parameters) {

            Object[] para;
            if (parameters instanceof Map) {
                PreparedSqlAndParameters sqlAndPara = toPreparedSqlAndPara(sql, (Map) parameters);
                para = sqlAndPara.paras;
            } else if (parameters instanceof Collection) {
                para = ((Collection) parameters).toArray();
            } else {
                para = (Object[]) parameters;
            }

            if (SHOWSQL) {
                log.debug("updateBatch--para:{}", Arrays.asList(para));
            }

            JdbcUtil.insertParameters(paramPreparedStatement, para);
            paramPreparedStatement.addBatch();
        }

    }

    public Object execute(PreparedStatement paramPreparedStatement) throws SQLException, PersistenceException {


        log.debug("executeBatch...");

        this.updateRows = paramPreparedStatement.executeBatch();
        paramPreparedStatement.clearBatch();

        return this.updateRows.length;
    }


    public void postProcess(PreparedStatement paramPreparedStatement) throws SQLException, PersistenceException {

    }


    private PreparedSqlAndParameters toPreparedSqlAndPara(String sql, Map paramMap) {
        return SqlParameterUtil.toPreparedSqlAndPara(sql, paramMap);
    }


}
