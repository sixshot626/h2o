package h2o.dao.sql;

import java.util.Map;

public interface SqlSource {

    String getSql();

    String getSql(Map<String, Object> data);

}
