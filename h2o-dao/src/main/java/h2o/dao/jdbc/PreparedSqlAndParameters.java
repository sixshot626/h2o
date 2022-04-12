package h2o.dao.jdbc;


public class PreparedSqlAndParameters {

    public final String sql;

    public final Object[] paras;

    public PreparedSqlAndParameters(String sql, Object[] paras) {
        this.sql = sql;
        this.paras = paras;
    }
}
