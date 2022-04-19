package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

public class MaxLen extends AbstractValidator implements Validator {


    private int len;

    public MaxLen(int len) {
        this.len = len;
    }


    @Override
    public boolean validateV(Object v) {

        if (v == null) {
            return true;
        }

        return ((String) v).length() <= len;

    }

}
