package h2o.dao.impl.proc;

import h2o.common.Tools;
import h2o.common.util.bean.ValOperate;
import h2o.common.util.bean.support.JoddBeanUtilVOImpl;
import h2o.common.util.lang.InstanceUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.proc.OrmProcessor;
import h2o.jodd.bean.BeanUtil;
import h2o.jodd.bean.BeanUtilBean;

import java.util.Map;


public class DefaultOrmProcessor implements OrmProcessor {

    private boolean declare  = false;

    private boolean silently = true;

    private boolean force    = false;

    private volatile boolean custom = false;


    @Override
    public <T> T proc(Map<String, Object> row, Class<T> clazz) throws DaoException {

        if (row == null) {
            return null;
        }

        try {
            if ( custom ) {

                ValOperate beanVo = new JoddBeanUtilVOImpl()
                        .setDeclare( declare )
                        .setForce( force )
                        .setSilently( silently );

                return new Row2BeanProcessor(clazz, Tools.bic.beanVo( beanVo )).toBean(row, createBean(clazz));

            } else {
                return new Row2BeanProcessor(clazz).toBean(row, createBean(clazz));
            }

        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

    protected <T> T createBean(Class<T> beanClazz) {
        return InstanceUtil.newInstance(beanClazz);
    }


    public void setDeclare(boolean declare) {
        this.declare = declare;
        this.custom = true;
    }

    public void setForce(boolean force) {
        this.force = force;
        this.custom = true;
    }

    public void setSilently(boolean silently) {
        this.silently = silently;
        this.custom = true;
    }
}
