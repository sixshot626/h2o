package h2o.common.concurrent.factory;

public abstract class AbstractInstanceFactory<I> implements InstanceFactory<I> {

    @Override
    public void free(Object id, I ins) {
        destroy(ins);
    }

    @Override
    public void destroy(I ins) {
    }

}
