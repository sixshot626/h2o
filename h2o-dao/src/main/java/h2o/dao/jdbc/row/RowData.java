package h2o.dao.jdbc.row;

import h2o.common.lang.K;
import h2o.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RowData extends AbstractMap<String, Object> implements Serializable {

    private static final long serialVersionUID = 803500399510042017L;


    private final Map<String,Object> realMap;
    private final Map<String,String> keyMapping;


    public RowData(Map<String, ?> m) {

        if ( m == null || m.isEmpty() ) {

            this.realMap    = Collections.EMPTY_MAP;
            this.keyMapping = Collections.EMPTY_MAP;

        } else {

            Map<String, Object> map = new HashMap<>();
            Map<String, String> km  = new HashMap<>();

            for (Map.Entry<String, ?> entry : m.entrySet()) {

                String _key = proc(entry.getKey());
                map.put( _key, entry.getValue());

                if ( _key.indexOf('_') > -1 ) {
                    km.put( proc2(_key) , _key );
                }

            }

            this.realMap    = Collections.unmodifiableMap(map);
            this.keyMapping = Collections.unmodifiableMap(km);

        }

    }


    private String proc(Object obj) {

        if (obj == null) {
            throw new IllegalArgumentException();
        }

        String key;
        if (obj instanceof String) {
            key = ((String) obj).trim();
        } else if (obj instanceof K) {
            key = ((K) obj).getValue();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException();
        }

        return key == null ? null : key.toLowerCase();
    }


    private String proc2( String key ) {
        return StringUtils.remove(StringUtils.deleteWhitespace(key) , "_");
    }



    private String procKey( Object obj ) {

        String key = proc( obj );

        String mKey = keyMapping.get( proc2(key) );

        return mKey == null ? key : mKey;

    }


    public int size()                        {return realMap.size();}
    public boolean isEmpty()                 {return realMap.isEmpty();}
    public boolean containsKey(Object key)   {return realMap.containsKey( procKey(key) );}
    public boolean containsValue(Object val) {return realMap.containsValue(val);}

    public Object get(Object key)                 {return realMap.get( procKey(key) );}

    public Object put(String key, Object value) {
        throw new UnsupportedOperationException();
    }
    public Object remove(Object key) {
        throw new UnsupportedOperationException();
    }
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException();
    }
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        return realMap.keySet();
    }

    @Override
    public Collection<Object> values() {
        return realMap.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return realMap.entrySet();
    }

    public boolean equals(Object o) {return o == this || realMap.equals(o);}
    public int hashCode()           {return realMap.hashCode();}
    public String toString()        {return realMap.toString();}

    // Override default methods in Map
    @Override
    public Object getOrDefault(Object k, Object defaultValue) {
        // Safe cast as we don't change the value
        return (realMap).getOrDefault( procKey(k), defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Object> action) {
        realMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Object, ?> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object putIfAbsent(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(String key, Object oldValue, Object newValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object replace(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object computeIfAbsent(String key, Function<? super String, ?> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object computeIfPresent(String key,
                                   BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object compute(String key,
                          BiFunction<? super String, ? super Object, ?> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object merge(String key, Object value,
                        BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
        throw new UnsupportedOperationException();
    }
}
