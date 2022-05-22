package h2o.dao.result;

import java.util.Map;

public final class RowDataGetter {

    private final Map<Object,Object> data;

    public RowDataGetter(Map<?, ?> data) {
        this.data = (Map<Object,Object>)data;
    }

    public <T> T get( Object key ) {
        return (T) data.get(key);
    }

    public <T> T getOrDefault(Object key, T defaultValue) {
        return (T) data.getOrDefault(key, defaultValue);
    }

}
