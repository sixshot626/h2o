package h2o.common.result;

import h2o.common.lang.NullableValue;

public interface TransResult<R> extends Response , NullableValue {

    boolean hasResult();

    R getResult();

}
