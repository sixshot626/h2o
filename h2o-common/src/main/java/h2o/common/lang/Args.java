package h2o.common.lang;

import java.util.Arrays;

public class Args<T> {

    public final T[] args;

    public Args(T[] args) {
        this.args = args;
    }

    public static <T> Args<T> by(T... args) {
        return new Args<>(args);
    }

    public T[] getArgs() {
        return args;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Args{");
        sb.append("args=").append(Arrays.toString(args));
        sb.append('}');
        return sb.toString();
    }
}
