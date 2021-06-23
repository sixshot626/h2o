package h2o.common.lang;

import java.util.Objects;

public final class NString implements OptionalValue<String>, Comparable<NString> , java.io.Serializable {

    private static final long serialVersionUID = -7152107032754248698L;

    public static final NString NULL = new NString();

    private final String value;

    public NString() {
        this.value = null;
    }

    public NString(String value ) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(NString o) {
        if ( this.isPresent() && o.isPresent() ) {
            return this.value.compareTo( o.value );
        } else if ( ( ! this.isPresent() ) && ( ! o.isPresent())  ) {
            return 0;
        } else {
            return this.isPresent() ? 1 : -1;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !( o instanceof NString)) return false;
        NString nString = (NString) o;
        return Objects.equals(value, nString.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return this.orElse("<null>");
    }

}
