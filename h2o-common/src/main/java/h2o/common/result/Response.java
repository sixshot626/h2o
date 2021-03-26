package h2o.common.result;

import h2o.common.lang.NBool;

public interface Response extends ErrorInfo {

    NBool ok();

    NBool finalState();

    String getCode();

    String getMsg();

    boolean hasException();

    Throwable getException();

}
