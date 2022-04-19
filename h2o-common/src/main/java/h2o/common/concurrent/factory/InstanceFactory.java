package h2o.common.concurrent.factory;

public interface InstanceFactory<I> {

    I create(Object id);

    void free(Object id, I ins);

    void destroy(I ins);

}
