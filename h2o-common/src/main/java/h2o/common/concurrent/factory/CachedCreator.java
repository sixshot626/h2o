package h2o.common.concurrent.factory;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class CachedCreator<T> {

    private final Supplier<T> defaultCreator;


    public CachedCreator() {
        this.defaultCreator = null;
    }
    public CachedCreator(Supplier<T> defaultCreator) {
        this.defaultCreator = defaultCreator;
    }

    private final AtomicReference<T> instanceRef = new AtomicReference<>();

    public T get( boolean create ) {
        T instance = instanceRef.get();
        if ( instance == null ) {
            if (defaultCreator == null ) {
                if ( create ) {
                    throw new RuntimeException("no defaultCreator");
                } else {
                    return null;
                }
            }
            instance = defaultCreator.get();
            instanceRef.compareAndSet(null,instance);
        }
        return instance;
    }

    public T get( Supplier<T> creator ) {
        T instance = instanceRef.get();
        if ( instance == null ) {
            instance = creator.get();
            instanceRef.compareAndSet(null,instance);
        }
        return instance;
    }

}
