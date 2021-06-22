package h2o.common.result;

import h2o.common.lang.OptionalValue;

public interface TransResult<R> extends Response {

    boolean isPresent();

    boolean hasResult();

    R getResult();

}
