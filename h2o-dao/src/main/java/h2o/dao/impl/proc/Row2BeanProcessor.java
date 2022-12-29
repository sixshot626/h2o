package h2o.dao.impl.proc;

import h2o.common.Tools;
import h2o.common.util.bean.BeanUtil;
import h2o.dao.structure.ColumnMeta;
import h2o.dao.structure.ColumnMetaUtil;
import h2o.dao.structure.TableStruct;
import h2o.dao.structure.TableStructParser;

import java.util.Map;

public class Row2BeanProcessor {

    private final BeanUtil beanUtil;

    private final TableStruct tableStruct;


    public Row2BeanProcessor(Class<?> beanClazz) {
        this(beanClazz, Tools.bic);
    }

    public Row2BeanProcessor(Class<?> beanClazz, BeanUtil beanUtil) {


        TableStruct _tableStruct = null;
        if (ColumnMetaUtil.hasTableAnnotation(beanClazz)) {
            _tableStruct = TableStructParser.parse( beanClazz );
        }
        tableStruct = _tableStruct;

        this.beanUtil = beanUtil;

    }

    public <T> T toBean(Map<?, ?> m, T bean) {

        if ( tableStruct == null ) {
            return beanUtil.beanCopy(m, bean);
        }

        String[] prepNames = beanUtil.analysePrepNames(bean);
        String[] srcpNames = new String[prepNames.length];

        for (int i = 0; i < prepNames.length; i++) {
            ColumnMeta col = tableStruct.findColumn(prepNames[i]);
            srcpNames[i] = col == null ? prepNames[i] : col.colName;
        }


        return beanUtil.beanCopy(m, bean, srcpNames, prepNames);

    }


}
