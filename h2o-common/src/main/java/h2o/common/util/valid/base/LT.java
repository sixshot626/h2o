package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

import java.util.Comparator;

public class LT extends AbstractCompare implements Validator {


    public LT(Object cm) {
        super(cm, null);
    }

    @SuppressWarnings("rawtypes")
    public LT(Object vm, Comparator c) {
        super(vm, c);
    }

    @Override
    public boolean validateV(Object v) {
        int d = compare(v);
        return d < 0;
    }

}
