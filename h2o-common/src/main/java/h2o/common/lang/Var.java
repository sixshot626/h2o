package h2o.common.lang;

public final class Var<T> implements OptionalValue<T> {

    public T value;

    public Var() {
    }

    public Var(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "<null>" : value.toString();
    }

}
