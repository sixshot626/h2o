package h2o.common.lang;


import h2o.apache.commons.lang.StringUtils;

import java.util.Objects;

public final class K implements Comparable<K>, java.io.Serializable {

    private static final long serialVersionUID = 2360167979097124087L;

    private final String value;

    public K(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException();
        }
        this.value = value.trim().toLowerCase();
    }


    public String name() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public static K k(String str) {
        return new K(str);
    }

    @Override
    public int compareTo(K o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        K k = (K) o;
        return value.equals(k.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }


    @Override
    public String toString() {
        return this.value;
    }
}
