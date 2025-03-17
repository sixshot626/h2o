package h2o.common.collection;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.lang.Key;

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

        if ( map == null ) {

            this.realMap    = new LinkedHashMap<>();
            this.keyMapping = new HashMap<>();

        } else {

            Map<String, String> km  = new HashMap<>();

            for (Map.Entry<String, V> entry : map.entrySet()) {

                String _key = entry.getKey();

                km.put( proc2(_key) , _key );

            }

            this.realMap    = map;
            this.keyMapping = km;

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

        if ( key == null ) {
            throw new IllegalArgumentException();
        }

        return key;
    }


    protected String proc2( String key ) {
        return cleanKey(key);
    }



    protected String procKey( Object obj ) {

        String key = proc( obj );

        String _key =  keyMapping.get( proc2(key) );

        return _key == null ? key : _key;

    }


    public static String cleanKey( String key ) {

        if ( key == null ) {
            return null;
        }

        return StringUtils.remove(
                StringUtils.remove(
                        StringUtils.remove(
                                StringUtils.remove(StringUtils.deleteWhitespace(key.toLowerCase())
                                        , '_'), '`') , '\"') , '\'');
    }




    public static <T> KeyMap<T> wrap(Map<String, T> map) {
        if ( map instanceof KeyMap ) {
            return (KeyMap<T>) map;
        }
        return new KeyMap<>(map);
    }




    public V dissoc( Object obj ) {

        String key = proc( obj );
        String key2 = proc2(key);

        String oldKay = keyMapping.remove( key2 );
        if ( oldKay != null ) {
            return realMap.remove( oldKay );
        } else {
            return null;
        }

    }


    public V assoc( Object obj, V value ) {

        String key = proc( obj );
        String key2 = proc2(key);

        String oldKay = keyMapping.put( key2 , key );

        V oldVal = null;
        if ( oldKay != null ) {
            oldVal = realMap.remove( oldKay );
        }
        realMap.put( key , value );

        return oldVal;

    }



    public Map<String,V> originalMap() {
        return this.realMap;
    }



    @Override
    public int size()                        {return realMap.size();}

    @Override
    public boolean isEmpty()                 {return realMap.isEmpty();}

    @Override
    public boolean containsKey(Object key)   {return realMap.containsKey( procKey(key) );}

    @Override
    public boolean containsValue(Object val) {return realMap.containsValue(val);}

    @Override
    public V get(Object key)                 {return realMap.get( procKey(key) );}

    @Override
    public V put(String key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
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
        if (this == o) return true;
        if (o == null ) return false;
        if (o instanceof KeyMap) {
            KeyMap<?> keyMap = (KeyMap<?>) o;
            return realMap.equals(keyMap.realMap);
        } else {
            return realMap.equals(o);
        }
    }

    @Override
    public int hashCode()           {return realMap.hashCode();}

    @Override
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
