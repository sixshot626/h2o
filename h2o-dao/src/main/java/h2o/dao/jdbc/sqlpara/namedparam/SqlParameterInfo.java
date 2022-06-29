package h2o.dao.jdbc.sqlpara.namedparam;

public class SqlParameterInfo {

    private String sql;

    private Object[] params;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
