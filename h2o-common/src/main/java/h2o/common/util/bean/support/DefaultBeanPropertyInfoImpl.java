package h2o.common.util.bean.support;

import h2o.common.util.bean.BeanPropertyInfo;

public class DefaultBeanPropertyInfoImpl implements BeanPropertyInfo {

    @Override
    public boolean coverAble(Object bean, String pn, Object val) {
        return val == null;
    }

}
