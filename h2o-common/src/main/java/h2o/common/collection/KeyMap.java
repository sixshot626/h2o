package h2o.common.collection;

import h2o.common.lang.Key;
import h2o.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class KeyMap<V> extends AbstractMap<String, V> implements Map<String,V> ,Serializable {

    private static final long serialVersionUID = 803500399510042017L;


    private final Map<String,V> realMap;
    private final Map<String,String> keyMapping;


    public KeyMap(Map<String, V> map) {

        if ( map == null || map.isEmpty() ) {

            this.realMap    = Collections.EMPTY_MAP;
            this.keyMapping = Collections.EMPTY_MAP;

        } else {

            Map<String, String> km  = new HashMap<>();

            for (Map.Entry<String, V> entry : map.entrySet()) {

                String _key = entry.getKey();

                km.put( proc2(_key) , _key );

            }

            this.realMap    = Collections.unmodifiableMap(map);
            this.keyMapping = Collections.unmodifiableMap(km);

        }

    }


    protected String proc(Object obj) {

        if (obj == null) {
            throw new IllegalArgumentException();
        }

        String key;
        if (obj instanceof String) {
            key = ((String) obj).trim();
        } else if (obj instanceof Key) {
            key = ((Key) obj).getValue();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException();
        }

        return key == null ? null : key;
    }


    protected String proc2( String key ) {
        return StringUtils.remove(StringUtils.deleteWhitespace(key.toLowerCase()) , "_");
    }



    protected String procKey( Object obj ) {

        String key = proc( obj );

        return keyMapping.get( proc2(key) );

    }


    public int size()                        {return realMap.size();}
    public boolean isEmpty()                 {return realMap.isEmpty();}
    public boolean containsKey(Object key)   {return realMap.containsKey( procKey(key) );}
    public boolean containsValue(Object val) {return realMap.containsValue(val);}

    public V get(Object key)                 {return realMap.get( procKey(key) );}

    public V put(String key, Object value) {
        throw new UnsupportedOperationException();
    }
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
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
    public Collection<V> values() {
        return realMap.values();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        return realMap.entrySet();
    }


    @Override
    public boolean equals(Object o) {
        return realMap.equals(o);
    }

    public int hashCode()           {return realMap.hashCode();}
    public String toString()        {return realMap.toString();}





    // Override default methods in Map
    @Override
    public V getOrDefault(Object k, V defaultValue) {
        // Safe cast as we don't change the value
        return (realMap).getOrDefault( procKey(k), defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super V> action) {
        realMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException();
    }


    @Override
    public V putIfAbsent(String key, Object value) {
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
    public V replace(String key, Object value) {
        throw new UnsupportedOperationException();
    }


    @Override
    public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException();
    }


    @Override
    public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }


    @Override
    public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }


    @Override
    public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException();
    }
}
