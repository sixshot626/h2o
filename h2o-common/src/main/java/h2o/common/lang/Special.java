package h2o.common.lang;

import h2o.common.util.lang.StringUtil;

import java.util.Objects;

public final class Special implements Comparable<Special>, java.io.Serializable , CharSequence {


    private static final long serialVersionUID = 4003380362220439161L;
    private final String value;

    public Special(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public static Special s(String str) {
        return new Special(str);
    }

    public static Special str(Object... strs) {
        return new Special(StringUtil.str( strs) );
    }

    @Override
    public int compareTo(Special o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Special s = (Special) o;
        return value.equals(s.value);
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
        return s(value.substring( start , end ));
    }


}
