package h2o.common.lang;

import java.util.Objects;

public class Gate<T> implements java.io.Serializable {

    private static final long serialVersionUID = -597329505385306301L;

    private static final Gate<?> EMPTY = new Gate<>(false , null);


    public final boolean ok;

    public final T value;

    public Gate(boolean ok, T value) {
        this.ok = ok;
        this.value = value;
    }

    public static <R> Gate<R> ok(R value) {
        return new Gate<>(true,value);
    }

    public static <R> Gate<R> empty() {
        @SuppressWarnings("unchecked")
        Gate<R> t = (Gate<R>) EMPTY;
        return t;
    }


    public boolean isOk() {
        return ok;
    }

    public T getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gate<?> gate = (Gate<?>) o;
        return ok == gate.ok && Objects.equals(value, gate.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ok, value);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Gate{");
        sb.append("ok=").append(ok);
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
