package h2o.common.result;

public interface TransResult<R> extends Response {

    boolean hasResult();

    R getResult();

}
