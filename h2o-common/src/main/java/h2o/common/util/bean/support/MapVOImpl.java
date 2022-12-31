package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;
import h2o.common.util.bean.ValOperate;

import java.util.Map;


public class MapVOImpl implements ValOperate {

    private final PreOperate<String> keyPreOp;


    public MapVOImpl() {
        this.keyPreOp = null;
    }

    public MapVOImpl(PreOperate<String> keyPreOp) {
        this.keyPreOp = keyPreOp;
    }

    @Override
    public Object get(Object target, String pName) {

        @SuppressWarnings("rawtypes")
        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = keyPreOp.doOperate(pName);
        }

        return m.get(pName);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set(Object target, String pName, Object val) {

        @SuppressWarnings("rawtypes")
        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = keyPreOp.doOperate(pName);
        }

        m.put(pName, val);

    }

    @SuppressWarnings({"rawtypes"})
    public Object remove(Object target, String pName) {

        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = keyPreOp.doOperate(pName);
        }

        return m.remove(pName);
    }

}
