package h2o.common.util.bean.support;

import h2o.common.util.bean.PreOperate;
import h2o.common.util.bean.ValOperate;

import java.util.Map;


public class MapVOImpl implements ValOperate, java.io.Serializable {


    private static final long serialVersionUID = 8205365150742989454L;

    private PreOperate<String> keyPreOp;

    public void setKeyPreOp(PreOperate<String> keyPreOp) {
        this.keyPreOp = keyPreOp;
    }

    public MapVOImpl() {
    }

    public MapVOImpl(PreOperate<String> keyPreOp) {
        this.setKeyPreOp(keyPreOp);
    }

    @SuppressWarnings("rawtypes")
    public Object get(Object target, String pName) {

        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = (String) keyPreOp.doOperate(pName);
        }

        return m.get(pName);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void set(Object target, String pName, Object val) {

        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = (String) keyPreOp.doOperate(pName);
        }

        m.put(pName, val);

    }

    @SuppressWarnings({"rawtypes"})
    public Object remove(Object target, String pName) {

        Map m = (Map) target;

        if (keyPreOp != null) {
            pName = (String) keyPreOp.doOperate(pName);
        }

        return m.remove(pName);
    }

}
