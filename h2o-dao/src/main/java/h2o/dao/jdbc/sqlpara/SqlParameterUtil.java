package h2o.dao.jdbc.sqlpara;

import h2o.common.Tools;
import h2o.common.lang.Key;
import h2o.common.util.bean.BeanUtil;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;
import h2o.dao.jdbc.sqlpara.namedparam.NamedParameterUtils;
import h2o.dao.jdbc.sqlpara.namedparam.SqlParameterInfo;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SqlParameterUtil {

    private final BeanUtil beanUtil;

    public SqlParameterUtil() {
        this(Tools.bnc);
    }


    public SqlParameterUtil(BeanUtil beanUtil) {
        this.beanUtil = beanUtil;
    }


    public Map<String, Object> toMap(Object... args) {

        Map<String, Object> m = MapBuilder.newMap();

        if (!CollectionUtil.argsIsBlank(args)) for (int i = 0; i < args.length; i++) {

            Object a = args[i];

            if (a == null) {
                continue;
            }

            if (otherToMapProc(a, m)) {
            } else if (a instanceof Map) {

                for (Map.Entry<?, ?> e : ((Map<?, ?>) a).entrySet()) {
                    m.put(e.getKey().toString(), valConvert(e.getValue()));
                }

            } else if (a instanceof String) {

                Object val = args[++i];
                m.put((String) a, valConvert(val));

            } else if (a instanceof Enum) {

                Object val = args[++i];
                m.put(((Enum) a).name(), valConvert(val));

            } else if (a instanceof Key) {

                Object val = args[++i];
                m.put(((Key) a).name(), valConvert(val));

            } else {

                Map<String, Object> sqlPara = beanUtil.bean2Map(a);
                for (Map.Entry<String, Object> e : sqlPara.entrySet()) {
                    m.put(e.getKey(), valConvert(e.getValue()));
                }
            }

        }

        return m;

    }

    @SuppressWarnings("rawtypes")
    protected Object valConvert(Object val) {

        if (val == null) {

            return null;

        } else if (val instanceof Enum) {

            return ((Enum) val).name();

        } else if (val instanceof Key) {

            return ((Key) val).name();

        } else if (val instanceof Collection) {

            Collection<Object> ol = (Collection) val;
            List<Object> nl = ListBuilder.newList(ol.size());

            for (Object l : ol) {
                nl.add(valConvert(l));
            }
            return nl;

        } else {

            return val;
        }

    }


    protected boolean otherToMapProc(Object arg, Map<String, Object> m) {
        return false;
    }


    @SuppressWarnings("rawtypes")
    public static PreparedSqlAndParameters toPreparedSqlAndPara(String sql, Map paramMap) {

        SqlParameterInfo sqlAndPara = NamedParameterUtils.mapPara2ObjectsPara(sql, paramMap);

        List<Object> para = ListBuilder.newList();
        for (Object value : sqlAndPara.getParams()) {
            if (value instanceof Collection) {
                Iterator entryIter = ((Collection) value).iterator();

                while (entryIter.hasNext()) {

                    Object entryItem = entryIter.next();
                    if (entryItem instanceof Object[]) {
                        Object[] expressionList = (Object[]) entryItem;

                        for (int m = 0; m < expressionList.length; m++) {
                            para.add(expressionList[m]);
                        }

                    } else {
                        para.add(entryItem);
                    }
                }
            } else {
                para.add(value);
            }
        }

        return new PreparedSqlAndParameters(sqlAndPara.getSql(), para.toArray());

    }


}
