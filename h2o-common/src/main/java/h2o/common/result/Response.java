package h2o.common.result;

public interface Response extends ErrorInfo {

    boolean isFinalState();

    boolean isSuccess();

    boolean isException();

    String getCode();

    String getMsg();

    Throwable getE();

    boolean isPresentResult();

    Object getResult();

}
