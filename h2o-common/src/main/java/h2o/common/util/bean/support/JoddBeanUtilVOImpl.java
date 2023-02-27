package h2o.common.util.bean.support;

import h2o.common.util.bean.ValOperate;
import h2o.jodd.bean.BeanUtilBean;


public class JoddBeanUtilVOImpl implements ValOperate {


    private final BeanUtilBean readUtil = new BeanUtilBean();

    private final BeanUtilBean writeUtil = new BeanUtilBean();

    public JoddBeanUtilVOImpl setSilently(boolean isSilently) {
        readUtil.silent(isSilently);
        writeUtil.silent(isSilently);
        return this;
    }


    public JoddBeanUtilVOImpl readSilently(boolean isSilently) {
        readUtil.silent(isSilently);
        return this;
    }

    public JoddBeanUtilVOImpl writeSilently(boolean isSilently) {
        writeUtil.silent(isSilently);
        return this;
    }

    public JoddBeanUtilVOImpl setForce(boolean isForce) {
        readUtil.forced(isForce);
        writeUtil.forced(isForce);
        return this;
    }

    public JoddBeanUtilVOImpl readForce(boolean isForce) {
        readUtil.forced(isForce);
        return this;
    }

    public JoddBeanUtilVOImpl writeForce(boolean isForce) {
        writeUtil.forced(isForce);
        return this;
    }



    public JoddBeanUtilVOImpl setDeclare(boolean isDeclare) {
        readUtil.declared(isDeclare);
        writeUtil.declared(isDeclare);
        return this;
    }

    public JoddBeanUtilVOImpl readDeclare(boolean isDeclare) {
        readUtil.declared(isDeclare);
        return this;
    }

    public JoddBeanUtilVOImpl writeDeclare(boolean isDeclare) {
        writeUtil.declared(isDeclare);
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
        return readUtil.getProperty(target, pName);
    }

    @Override
    public void set(Object target, String pName, Object val) {
        writeUtil.setProperty(target, pName, val);
    }

}
