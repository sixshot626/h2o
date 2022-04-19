package h2o.common.util.valid;

public interface Validator {

    String getMessage();

    void setK(String k);

    String getK();

    boolean validate(Object bean);

}
