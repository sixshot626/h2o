package h2o.common.result;

import java.util.Optional;

public interface Response extends ErrorInfo {

    Optional<Boolean> isFinalState();

    Optional<Boolean> isSuccess();

    boolean isException();

    String getCode();

    String getMsg();

    Throwable getE();

    boolean isPresentResult();

    Object getResult();

}
