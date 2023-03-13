package h2o.common.lang;


import h2o.apache.commons.lang.StringUtils;

import java.util.Objects;

public final class Key implements Comparable<Key>, java.io.Serializable , CharSequence {

    private static final long serialVersionUID = 2360167979097124087L;

    private final String value;

    public Key(String value) {
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

    public static Key k(String str) {
        return new Key(str);
    }

    @Override
    public int compareTo(Key o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key k = (Key) o;
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


    // CharSequence

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return k(value.substring( start , end ));
    }


}
