package h2o.common.util.bean.support;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.bean.BeanPropertyInfo;

public class StringBlankBeanPropertyInfoImpl extends DefaultBeanPropertyInfoImpl implements BeanPropertyInfo {

    @Override
    public boolean coverAble(Object bean, String pn, Object val) {
        if (val instanceof String) {
            return StringUtils.isBlank((String) val);
        }
        return super.coverAble(bean, pn, val);
    }


}
