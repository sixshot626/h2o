package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

import java.util.Comparator;

public class GT extends AbstractCompare implements Validator {


    public GT(Object cm) {
        super(cm, null);
    }

    @SuppressWarnings("rawtypes")
    public GT(Object vm, Comparator c) {
        super(vm, c);
    }

    @Override
    public boolean validateV(Object v) {
        int d = compare(v);
        return d > 0;
    }

}
