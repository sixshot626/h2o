package h2o.common.util.bean.support;

import h2o.common.util.bean.ValOperate;
import h2o.jodd.bean.BeanUtilBean;


public class JoddBeanUtilVOImpl implements ValOperate {


    private final BeanUtilBean beanUtilBean = new BeanUtilBean();

    public JoddBeanUtilVOImpl setSilently(boolean isSilently) {
        beanUtilBean.silent(isSilently);
        return this;
    }

    public JoddBeanUtilVOImpl setForce(boolean isForce) {
        beanUtilBean.forced(isForce);
        return this;
    }

    public JoddBeanUtilVOImpl setDeclare(boolean isDeclare) {
        beanUtilBean.declared(isDeclare);
        return this;
    }

    public JoddBeanUtilVOImpl() {
        this(false, false);
    }

    public JoddBeanUtilVOImpl(boolean isSilently) {
        this(isSilently, false);
    }

    public JoddBeanUtilVOImpl(boolean isSilently, boolean isForce) {
        this.setSilently(isSilently);
        this.setForce(isForce);
    }

    @Override
    public Object get(Object target, String pName) {
        return beanUtilBean.getProperty(target, pName);
    }

    @Override
    public void set(Object target, String pName, Object val) {
        beanUtilBean.setProperty(target, pName, val);
    }

}
