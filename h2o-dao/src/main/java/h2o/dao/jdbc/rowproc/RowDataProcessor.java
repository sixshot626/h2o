package h2o.dao.jdbc.rowproc;

import java.util.Map;

public interface RowDataProcessor {
	
	Map<String,Object> dataProc(Map<String, Object> m);

}
