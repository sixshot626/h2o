package h2o.common.lang;

import java.util.Objects;

public class SString implements NullableValue , Comparable<SString> , java.io.Serializable {

    private static final long serialVersionUID = -4583244600596748522L;

    protected final String value;

    public SString() {
        this.value = null;
    }

    public SString( String value ) {
        this.value = value;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    public String getValue() {
        return value;
    }

    public String get() {

        if ( this.isPresent() ) {
            return value;
        }

        throw new IllegalStateException();

    }

    public String orElse(String other) {
        return this.isPresent() ? value : other;
    }

    @Override
    public int compareTo(SString o) {
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
        if (o == null || !( o instanceof SString )) return false;
        SString sString = (SString) o;
        return Objects.equals(value, sString.value);
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
