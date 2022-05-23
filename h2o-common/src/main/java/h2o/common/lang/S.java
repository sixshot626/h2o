package h2o.common.lang;

import java.util.Objects;

public final class S implements Comparable<S>, java.io.Serializable {


    private static final long serialVersionUID = 4003380362220439161L;
    private final String value;

    public S(String value) {
        if (value == null) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }


    public String getValue() {
        return value;
    }

    public static S s(String str) {
        return new S(str);
    }

    public static S str(Object... strs) {

        StringBuilder sb = new StringBuilder();

        if (strs != null || strs.length > 0 ) {
            for (Object str : strs) {
                if ( str != null ) {
                    if (str instanceof OptionalValue) {
                        if (((OptionalValue<?>) str).isPresent()) {
                            sb.append(str);
                        }
                    } else if (str instanceof CharSequence) {
                        sb.append((CharSequence) str);
                    } else {
                        sb.append(str);
                    }
                }
            }
        }

        return new S(sb.toString());

    }


    @Override
    public int compareTo(S o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        S s = (S) o;
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
}
