package h2o.common.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Var<T> {

	public T v;

    public Var() {
    }

    public Var(T v) {
        this.v = v;
    }




    public boolean isPresent() {
        return v != null;
    }

    public T getValue() {
        return v;
    }

    public T get() {

        if ( this.isPresent() ) {
            return v;
        }

        throw new IllegalStateException();
    }

    public T orElse( T other) {
        return v == null ? other : v;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Var<?> var = (Var<?>) o;

        return new EqualsBuilder()
                .append(v, var.v)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(v)
                .toHashCode();
    }

    @Override
	public String toString() {
		return v == null ? null : v.toString();
	}
	

}
