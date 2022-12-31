package h2o.dao.impl.proc;

import h2o.dao.proc.RowProcessor;

import java.util.Map;

public class DefaultRowProcessor implements RowProcessor {

    @Override
    public Map<String, Object> proc(Map<String, Object> m) {
        return m;
    }

}
