package h2o.dao.jdbc.row;

import java.util.Map;

public interface RowDataProcessor {

    Map<String, Object> dataProc(Map<String, Object> m);

}
