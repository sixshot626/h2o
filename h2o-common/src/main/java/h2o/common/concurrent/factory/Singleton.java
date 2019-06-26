package h2o.common.concurrent.factory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class Singleton<T> {

    private final Lock lock = new ReentrantLock();

    private volatile T instance = null;

    public T get() {
        T ins = instance;
        if ( ins == null ) {
            lock.lock();
            try {
                ins = instance;
                if ( ins == null ) {
                    ins = instance = create();
                }
            }finally {
                lock.unlock();
            }
        }

        return ins;
    }

    abstract T create();

}
