package h2o.common.util.valid.base;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.valid.Validator;

public class NotBlank extends AbstractValidator implements Validator {


    private boolean nullAble = false;

    public NotBlank() {
    }

    public NotBlank(boolean nullAble) {
        this.nullAble = nullAble;
    }

    @Override
    public boolean validateV(Object v) {

        if (v == null) {
            return nullAble;
        }

        return StringUtils.isNotBlank((String) v);

    }

}
