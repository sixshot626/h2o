package h2o.flow.pvm.runtime;

import java.util.*;

public class RuntimeScopeObject implements java.io.Serializable {

    private static final long serialVersionUID = -4518026306322958517L;

    private Map<String,Object> data;

    public Object get(Object key) {

        if ( data == null ) {
            return null;
        }

        return data.get(key);
    }

    public Object put(String key, Object value) {

        if ( data == null ) {
            this.data = new HashMap<>();
        }

        return data.put(key, value);
    }

    public Object remove(Object key) {

        if ( data == null ) {
            return null;
        }

        return data.remove(key);
    }

    public void putAll(Map<? extends String, ?> m) {

        if ( data == null ) {
            this.data = new HashMap<>();
        }

        data.putAll(m);
    }

    public void clear() {

        if ( this.data != null ) {
            data.clear();
        }

    }

    public Set<String> keySet() {

        if ( this.data == null ) {
            return Collections.emptySet();
        }

        return data.keySet();
    }

    public Collection<Object> values() {

        if ( this.data == null ) {
            return Collections.emptyList();
        }

        return data.values();
    }

    public Set<Map.Entry<String, Object>> entrySet() {

        if ( this.data == null ) {
            return Collections.emptySet();
        }

        return data.entrySet();
    }

    public Object getOrDefault(Object key, Object defaultValue) {

        if ( this.data == null ) {
            return defaultValue;
        }

        return data.getOrDefault(key, defaultValue);
    }

    public Map<String,Object> toMap() {

        if ( this.data == null ) {
            return Collections.emptyMap();
        }

        return Collections.unmodifiableMap( this.data );
    }
}
