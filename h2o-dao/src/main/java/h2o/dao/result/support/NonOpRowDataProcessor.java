package h2o.dao.result.support;

import h2o.dao.result.RowDataProcessor;

import java.util.Map;

public class NonOpRowDataProcessor implements RowDataProcessor {

    public Map<String, Object> dataProc(Map<String, Object> m) {
        return m;
    }

}
