package h2o.dao.impl.proc;

import h2o.common.util.lang.InstanceUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.proc.OrmProcessor;

import java.util.Map;


public class DefaultOrmProcessor implements OrmProcessor {


    @Override
    public <T> T proc(Map<String, Object> row, Class<T> clazz) throws DaoException {

        if (row == null) {
            return null;
        }

        try {
            return new DbMap2BeanProcessor(clazz).toBean(row, createBean(clazz));
        } catch (Exception e) {
            throw new DaoException(e);
        }

    }

    protected <T> T createBean(Class<T> beanClazz) {
        return InstanceUtil.newInstance(beanClazz);
    }

}
