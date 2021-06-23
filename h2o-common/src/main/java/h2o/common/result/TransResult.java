package h2o.common.result;

public interface TransResult<R> extends Response {

    boolean isPresent();

    boolean hasResult();

    R getResult();

}
