package h2o.dao.orm;

import java.util.Map;

public interface ArgProcessor {

    Map<String, Object> proc(Object... args);

}
