package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

import java.util.Comparator;


public abstract class AbstractCompare extends AbstractValidator implements Validator {

    protected Object vm;


    @SuppressWarnings("rawtypes")
    protected Comparator c;

    @SuppressWarnings("rawtypes")
    public AbstractCompare(Object vm, Comparator c) {
        this.vm = vm;
        this.c = c;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected int compare(Object v) {
        return c == null ? -((Comparable) vm).compareTo(v) : c.compare(v, vm);
    }

}
