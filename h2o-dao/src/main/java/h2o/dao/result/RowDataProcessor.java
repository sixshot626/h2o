package h2o.dao.result;

import java.util.Map;

public interface RowDataProcessor {

    Map<String, Object> dataProc(Map<String, Object> m);

}
