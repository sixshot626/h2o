package h2o.common.result;

public interface TransStatus<S> extends Response {

    S getStatus();

}
