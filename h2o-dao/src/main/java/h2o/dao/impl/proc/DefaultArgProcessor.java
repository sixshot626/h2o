package h2o.dao.impl.proc;


import h2o.common.Tools;
import h2o.common.util.bean.BeanUtil;
import h2o.dao.jdbc.sqlpara.SqlParameterUtil;
import h2o.dao.proc.ArgProcessor;

import java.util.Map;


public class DefaultArgProcessor implements ArgProcessor {

    private final SqlParameterUtil sqlParameterUtil;

    public DefaultArgProcessor() {
        this(Tools.bnc);
    }


    public DefaultArgProcessor(BeanUtil beanUtil) {

        this.sqlParameterUtil = new SqlParameterUtil(beanUtil) {
            @Override
            protected boolean otherToMapProc(Object arg, Map<Object, Object> m) {
                return otherProc(arg, m);
            }

        };

    }


    @Override
    public Map<String, Object> proc(Object... args) {
        return sqlParameterUtil.toMap(args);
    }

    protected boolean otherProc(Object arg, Map<Object, Object> m) {
        return false;
    }


}
