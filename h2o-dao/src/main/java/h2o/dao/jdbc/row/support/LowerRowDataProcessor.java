package h2o.dao.jdbc.row.support;

import h2o.common.collection.IgnoreCaseMap;
import h2o.dao.jdbc.row.RowDataProcessor;

import java.util.Map;

public class LowerRowDataProcessor implements RowDataProcessor {

    public Map<String, Object> dataProc(Map<String, Object> m) {
        return m == null ? null : new IgnoreCaseMap<Object>(m, "LOWER");
    }

}
