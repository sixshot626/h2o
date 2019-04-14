package h2o.common.util.lang;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Var<T> {

	public T v;

    public Var() {
    }

    public Var(T v) {
        this.v = v;
    }

    public T get() {
        return v;
    }

    public void set(T v) {
        this.v = v;
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
