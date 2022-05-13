package h2o.common.collection;

import h2o.common.util.bean.support.CasePreOperateImpl;
import h2o.common.util.collection.MapBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IgnoreCaseMap<V> implements Map<String, V>, java.io.Serializable {

    private static final long serialVersionUID = -8968989416226428493L;

    public static final String LOWER = CasePreOperateImpl.LOWER;
    public static final String UPPER = CasePreOperateImpl.UPPER;


    private final Map<String, V> realMap = MapBuilder.newMap();

    private final CasePreOperateImpl caseOperate;


    public IgnoreCaseMap(Map<String, V> m) {
        this(m, UPPER);
    }

    public IgnoreCaseMap(Map<String, V> m, String toCase) {
        this.caseOperate = new CasePreOperateImpl(toCase);
        this.putAll(m);
    }

    private String ignoreCaseKey(Object key) {
        return key == null ? null : this.caseOperate.doOperate((String) key);
    }


    @Override
    public int size() {
        return realMap.size();
    }

    @Override
    public boolean isEmpty() {
        return realMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return realMap.containsKey( ignoreCaseKey(key) );
    }

    @Override
    public boolean containsValue(Object value) {
        return realMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return realMap.get( ignoreCaseKey(key) );
    }

    @Override
    public V put(String key, V value) {
        return realMap.put( ignoreCaseKey(key) , value);
    }

    @Override
    public V remove(Object key) {
        return realMap.remove( ignoreCaseKey(key) );
    }

    public void putAll(Map<? extends String, ? extends V> m) {

        Map<String, V> icm = MapBuilder.newMap();
        for (Entry<? extends String, ? extends V> e : m.entrySet()) {
            icm.put(ignoreCaseKey(e.getKey()), e.getValue());
        }

        realMap.putAll(icm);
    }

    public void clear() {
        realMap.clear();
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

    @Override
    public int hashCode() {
        return realMap.hashCode();
    }

    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return realMap.getOrDefault( ignoreCaseKey(key) , defaultValue);
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super V> action) {
        realMap.forEach(action);
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super V, ? extends V> function) {
        realMap.replaceAll(function);
    }

    @Override
    public V putIfAbsent(String key, V value) {
        return realMap.putIfAbsent( ignoreCaseKey(key) , value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return realMap.remove( ignoreCaseKey(key) , value);
    }

    @Override
    public boolean replace(String key, V oldValue, V newValue) {
        return realMap.replace( ignoreCaseKey(key) , oldValue, newValue);
    }

    @Override
    public V replace(String key, V value) {
        return realMap.replace( ignoreCaseKey(key) , value);
    }

    @Override
    public V computeIfAbsent(String key, Function<? super String, ? extends V> mappingFunction) {
        return realMap.computeIfAbsent( ignoreCaseKey(key) , mappingFunction);
    }

    @Override
    public V computeIfPresent(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return realMap.computeIfPresent( ignoreCaseKey(key) , remappingFunction);
    }

    @Override
    public V compute(String key, BiFunction<? super String, ? super V, ? extends V> remappingFunction) {
        return realMap.compute( ignoreCaseKey(key) , remappingFunction);
    }

    @Override
    public V merge(String key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return realMap.merge( ignoreCaseKey(key) , value, remappingFunction);
    }
}
