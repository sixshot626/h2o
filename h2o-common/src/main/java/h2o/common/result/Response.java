package h2o.common.result;

public interface Response extends ErrorInfo {

    TriState ok();

    TriState finalState();

    String getCode();

    String getMsg();

    boolean hasException();

    Throwable getException();

}
