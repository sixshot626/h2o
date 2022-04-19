package h2o.common.lang.tuple;

import java.util.Objects;

public class Entry<K, V> implements java.io.Serializable {

    private static final long serialVersionUID = -8304096067686246637L;

    public final K key;

    public final V value;


    public Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry<?, ?> entry = (Entry<?, ?>) o;
        return Objects.equals(key, entry.key) && Objects.equals(value, entry.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return String.format("Entry[key=%s, value=%s]", key, value);
    }


}
