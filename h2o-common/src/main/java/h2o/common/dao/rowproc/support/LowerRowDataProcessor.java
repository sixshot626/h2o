package h2o.common.dao.rowproc.support;

import h2o.common.collections.IgnoreCaseMap;
import h2o.common.dao.rowproc.RowDataProcessor;

import java.util.Map;

public class LowerRowDataProcessor implements RowDataProcessor {

	public Map<String, Object> dataProc(Map<String, Object> m) {		
		return m == null ? null : new IgnoreCaseMap<Object>(m, "LOWER");
	}

}
