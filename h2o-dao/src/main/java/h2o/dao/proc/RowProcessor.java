package h2o.dao.proc;

import java.util.Map;

public interface RowProcessor {

    Map<String, Object> proc(Map<String, Object> m);

}
