package h2o.common.lang;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Row<E extends Enum> implements Serializable {

    private static final long serialVersionUID = -7864995458948805128L;

    private final Map<Object,Object> data;

    public Row() {
        this.data = new LinkedHashMap<>();
    }

    public Row(Map<?, ?> data) {
        this.data = (Map<Object,Object>)data;
    }

    public <R> R get( E key ) {
        return (R) data.get( key );
    }

    public <R> R put( E key , Object value ) {
        return (R) data.put( key , value );
    }

    public Map<Object,Object> data() {
        return this.data;
    }


    @Override
    public String toString() {
        return data == null ? "<null>" : data.toString();
    }
}
