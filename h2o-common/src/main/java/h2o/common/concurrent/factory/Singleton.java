package h2o.common.concurrent.factory;

import java.util.function.Supplier;

public final class Singleton<T> {

    private final Supplier<T> creator;

    public Singleton(Supplier<T> creator) {
        this.creator = creator;
    }

    private volatile T instance;

    public T get() {

        T ins = instance;
        if (ins == null) {
            synchronized (this) {
                ins = instance;
                if (ins == null) {
                    ins = instance = creator.get();
                }
            }
        }

        return ins;
    }


}
