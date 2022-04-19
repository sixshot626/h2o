package h2o.common.util.bean.support;

import h2o.common.exception.ExceptionUtil;
import h2o.common.util.bean.ValOperate;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BeanUtilVOImpl implements ValOperate, java.io.Serializable {

    private static final Logger log = LoggerFactory.getLogger(BeanUtilVOImpl.class.getName());


    private static final long serialVersionUID = 7101963633193511412L;

    private boolean isSilently;

    public void setSilently(boolean isSilently) {
        this.isSilently = isSilently;
    }

    public BeanUtilVOImpl() {
        this(false);
    }

    public BeanUtilVOImpl(boolean isSilently) {
        this.isSilently = isSilently;
    }

    public Object get(Object target, String pName) {
        try {
            return PropertyUtils.isReadable(target, pName) ? PropertyUtils.getProperty(target, pName) : null;
        } catch (Exception e) {
            log.debug("get", e);
            if (this.isSilently) {
                return null;
            } else {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }
    }

    public void set(Object target, String pName, Object val) {
        try {
            if (PropertyUtils.isWriteable(target, pName)) {
                PropertyUtils.setProperty(target, pName, val);
            }
        } catch (Exception e) {
            log.debug("set", e);
            if (!this.isSilently) {
                throw ExceptionUtil.toRuntimeException(e);
            }
        }
    }

}
