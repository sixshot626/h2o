package h2o.dao.proc;

import java.util.Map;

public interface ArgProcessor {

    Map<String, Object> proc(Object... args);

}
