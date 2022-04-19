package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

public class MinLen extends AbstractValidator implements Validator {

    private boolean nullAble = false;

    private int len;

    public MinLen(int len) {
        this.len = len;
    }

    public MinLen(int len, boolean nullAble) {
        this.len = len;
        this.nullAble = nullAble;
    }

    @Override
    public boolean validateV(Object v) {

        if (v == null) {
            return nullAble;
        }

        return ((String) v).length() <= len;

    }

}
