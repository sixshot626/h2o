package h2o.common.util.bean;

public interface ValOperate {

    Object get(Object target, String pName);

    void set(Object target, String pName, Object val);

}
