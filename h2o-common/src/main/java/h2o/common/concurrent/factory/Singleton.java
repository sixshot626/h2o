package h2o.common.concurrent.factory;

public abstract class Singleton<T> {

    private volatile T instance;

    public T get() {

        T ins = instance;
        if ( ins == null ) {
            synchronized (this) {
                ins = instance;
                if ( ins == null ) {
                    ins = create();
                    instance = ins;
                }
            }
        }

        return ins;
    }

    abstract T create();

}
