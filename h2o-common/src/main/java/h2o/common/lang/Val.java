package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.function.Supplier;

public final class Val<T> implements OptionalValue<T>, java.io.Serializable {

    private static final long serialVersionUID = -5406301526511160202L;
    
    private static final Val<?> EMPTY = new Val<>();

    private  final T value;

    private final boolean setted;

    private Val() {
        this.value = null;
        this.setted = false;
    }

    public Val(T value) {
        this.value = value;
        this.setted = true;
    }


    public static<T> Val<T> empty() {
        @SuppressWarnings("unchecked")
        Val<T> t = (Val<T>) EMPTY;
        return t;
    }

    public boolean isSetted() {
        return setted;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Val<?> val = (Val<?>) o;

        return new EqualsBuilder().append(value, val.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return value == null ? "<null>" : value.toString();
    }

}
