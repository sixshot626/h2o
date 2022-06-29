package h2o.dao.impl.proc;

import h2o.common.collection.IgnoreCaseMap;
import h2o.dao.proc.RowProcessor;

import java.util.Map;

public class LowerRowProcessor implements RowProcessor {

    public Map<String, Object> proc(Map<String, Object> m) {
        return m == null ? null : new IgnoreCaseMap<Object>(m, IgnoreCaseMap.LOWER);
    }

}
