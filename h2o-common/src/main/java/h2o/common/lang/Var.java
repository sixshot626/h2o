package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.NoSuchElementException;
import java.util.function.Supplier;

public final class Var<T> implements OptionalValue<T>, java.io.Serializable {

    private static final long serialVersionUID = -1525947429459499316L;

    private T value;

    private long version;

    public Var() {
        this.value = null;
        this.version = 0L;
    }

    public Var(T v) {
        this.value = v;
        this.version = 1L;
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
        this.version++;
    }

    public Var<T> update( T value ) {
        Var<T> newVar = new Var<>(value);
        newVar.version = this.version + 1;
        return newVar;
    }

    public long getVersion() {
        return version;
    }

    public boolean isSetted() {
        return this.version != 0;
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
