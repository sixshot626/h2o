package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Var<T> implements NullableValue , java.io.Serializable {

    private static final long serialVersionUID = -1525947429459499316L;

    private static final Var<?> EMPTY = new Var<>();

    private T value;

    private boolean setted;

    private Var() {
    }

    public Var(T v) {
        this.value = v;
        this.setted = true;
    }

    public static<T> Var<T> empty() {
        @SuppressWarnings("unchecked")
        Var<T> t = (Var<T>) EMPTY;
        return t;
    }

    @Override
    public boolean isPresent() {
        return value != null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.setted = true;
    }

    public boolean isSetted() {
        return setted;
    }


    public T get() {

        if ( this.isPresent() ) {
            return value;
        }

        throw new IllegalStateException();
    }

    public T orElse( T other) {
        return value == null ? other : value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || !(o instanceof Var)) return false;

        Var<?> var = (Var<?>) o;

        return new EqualsBuilder()
                .append(value, var.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(value)
                .toHashCode();
    }

    @Override
	public String toString() {
		return value == null ? "<null>" : value.toString();
	}
	

}
