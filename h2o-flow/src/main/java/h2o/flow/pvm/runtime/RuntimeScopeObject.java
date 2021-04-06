package h2o.flow.pvm.runtime;

import java.util.HashMap;
import java.util.Map;

public class RuntimeScopeObject implements java.io.Serializable {

    private static final long serialVersionUID = -4518026306322958517L;

    public final Map<String,Object> data = new HashMap<String, Object>(0);

}
