package h2o.common.result;

import h2o.common.lang.EBoolean;

public interface Response extends ErrorInfo {

    EBoolean ok();

    EBoolean finalState();

    String getCode();

    String getMsg();

    boolean hasException();

    Throwable getException();

}
