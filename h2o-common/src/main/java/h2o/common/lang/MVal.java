package h2o.common.lang;

import java.util.Objects;

public final class MVal<T>  implements OptionalValue<T> , java.io.Serializable {

    private static final long serialVersionUID = 8365534569568378627L;


    private static final MVal<?> EMPTY = new MVal<>(null,null);



    public final T value;

    public final String msg;


    public MVal(T value , String msg) {
        this.value = value;
        this.msg = msg;
    }


    public static <T> MVal<T> empty() {
        @SuppressWarnings("unchecked")
        MVal<T> t = (MVal<T>) EMPTY;
        return t;
    }



    @Override
    public T getValue() {
        return value;
    }

    public String getMsg() {
        return msg;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MVal<?> mVal = (MVal<?>) o;
        return Objects.equals(value, mVal.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value == null ? "<null>" : value.toString();
    }




}
