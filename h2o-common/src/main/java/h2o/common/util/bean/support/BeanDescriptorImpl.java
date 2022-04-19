package h2o.common.util.bean.support;

import h2o.common.exception.ExceptionUtil;
import h2o.common.util.bean.BeanDescriptor;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class BeanDescriptorImpl implements BeanDescriptor {

    private static final Logger log = LoggerFactory.getLogger(BeanDescriptorImpl.class.getName());

    private boolean excludeClass = true;


    public void setExcludeClass(boolean excludeClass) {
        this.excludeClass = excludeClass;
    }

    public BeanDescriptorImpl() {
    }

    public BeanDescriptorImpl(boolean excludeClass) {
        this.excludeClass = excludeClass;
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    public String[] getPrepNames(Object bean) {
        try {
            Set ps = BeanUtils.describe(bean).keySet();
            if (ps == null || ps.size() == 0) {
                return new String[0];
            }

            if (excludeClass) {
                ps.remove("class");
            }

            return (String[]) ps.toArray(new String[ps.size()]);

        } catch (Exception e) {
            log.debug("getPrepNames", e);
            throw ExceptionUtil.toRuntimeException(e);
        }
    }

}
