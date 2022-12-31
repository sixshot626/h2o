package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

public abstract class AbstractValidator implements Validator {


    private String message;

    private String k;


    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getK() {
        return k;
    }

    @Override
    public void setK(String k) {
        this.k = k;
    }

    public Validator v(String k, String message) {
        this.k = k;
        this.message = message;

        return this;
    }

    @Override
    public boolean validate(Object bean) {
        return this.validateV(h2o.jodd.bean.BeanUtil.silent.getProperty(bean, k));
    }

    protected boolean validateV(Object v) {
        return false;
    }


}
