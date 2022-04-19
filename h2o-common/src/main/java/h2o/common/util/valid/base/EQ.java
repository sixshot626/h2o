package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

import java.util.Comparator;

public class EQ extends AbstractCompare implements Validator {


    public EQ(Object cm) {
        super(cm, null);
    }

    @SuppressWarnings("rawtypes")
    public EQ(Object vm, Comparator c) {
        super(vm, c);
    }

    @Override
    public boolean validateV(Object v) {
        if (c == null) {
            return vm.equals(v);
        } else {
            return compare(v) == 0;
        }
    }

}
