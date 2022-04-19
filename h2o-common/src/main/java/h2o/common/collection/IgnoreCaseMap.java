package h2o.common.collection;

import h2o.common.util.bean.support.CasePreOperateImpl;
import h2o.common.util.collection.MapBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

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

    public int size() {
        return realMap.size();
    }

    public boolean isEmpty() {
        return realMap.isEmpty();
    }


    public boolean containsKey(Object key) {
        return realMap.containsKey(ignoreCaseKey(key));
    }

    public boolean containsValue(Object value) {
        return realMap.containsValue(value);
    }

    public V get(Object key) {
        return realMap.get(ignoreCaseKey(key));
    }

    public V put(String key, V value) {
        return realMap.put(ignoreCaseKey(key), value);
    }

    public V remove(Object key) {
        return realMap.remove(ignoreCaseKey(key));
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

    public Set<String> keySet() {
        return realMap.keySet();
    }

    public Collection<V> values() {
        return realMap.values();
    }

    public Set<Entry<String, V>> entrySet() {
        return realMap.entrySet();
    }

    public boolean equals(Object o) {
        return realMap.equals(o);
    }

    public int hashCode() {
        return realMap.hashCode();
    }

    @Override
    public String toString() {
        return realMap.toString();
    }


}
