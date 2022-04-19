package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

import java.util.Comparator;

public class LTE extends AbstractCompare implements Validator {


    public LTE(Object cm) {
        super(cm, null);
    }

    @SuppressWarnings("rawtypes")
    public LTE(Object vm, Comparator c) {
        super(vm, c);
    }

    @Override
    public boolean validateV(Object v) {
        int d = compare(v);
        return d <= 0;
    }

}
