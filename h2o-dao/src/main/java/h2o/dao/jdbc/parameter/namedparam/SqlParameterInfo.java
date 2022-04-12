package h2o.dao.jdbc.parameter.namedparam;

public class SqlParameterInfo {

    private String sql;

    private Object[] params;

    private int[] paramTypes;

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

    public int[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(int[] paramTypes) {
        this.paramTypes = paramTypes;
    }
}
