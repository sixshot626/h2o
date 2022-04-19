package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

public class NotNull extends AbstractValidator implements Validator {

    @Override
    public boolean validateV(Object v) {
        return v != null;
    }

}
