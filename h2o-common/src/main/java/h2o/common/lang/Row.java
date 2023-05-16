package h2o.common.lang;

import java.io.Serializable;
import java.util.Map;

public class Row<E extends Enum> implements Serializable {

    private static final long serialVersionUID = -7864995458948805128L;

    private final Map<Object,Object> data;

    public Row(Map<?, ?> data) {
        this.data = (Map<Object,Object>)data;
    }

    public <R> R get( E key ) {
        return (R) data.get( key );
    }

    public Map<Object,Object> data() {
        return this.data;
    }


    @Override
    public String toString() {
        return data.toString();
    }
}
