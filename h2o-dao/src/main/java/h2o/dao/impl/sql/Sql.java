package h2o.dao.impl.sql;

import h2o.dao.sql.SqlSource;

import java.util.Map;

public class Sql implements SqlSource {

    private final String sql;

    public Sql(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }

    @Override
    public String getSql(Map<String, Object> data) {
        return sql;
    }

    @Override
    public String toString() {
        return sql;
    }


}
