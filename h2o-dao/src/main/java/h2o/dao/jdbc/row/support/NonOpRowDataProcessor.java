package h2o.dao.jdbc.row.support;

import h2o.dao.jdbc.row.RowDataProcessor;

import java.util.Map;

public class NonOpRowDataProcessor implements RowDataProcessor {

    public Map<String, Object> dataProc(Map<String, Object> m) {
        return m;
    }

}
