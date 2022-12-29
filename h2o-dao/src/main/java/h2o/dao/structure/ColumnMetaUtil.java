package h2o.dao.structure;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.util.collection.CollectionUtil;
import h2o.dao.annotation.Column;
import h2o.dao.annotation.PK;
import h2o.dao.annotation.Table;
import h2o.dao.annotation.Unique;
import h2o.dao.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class ColumnMetaUtil {

    private static final Logger log = LoggerFactory.getLogger(ColumnMetaUtil.class.getName());

    private ColumnMetaUtil() {
    }

    public static boolean hasTableAnnotation(Object bean) {

        if (bean == null) {
            return false;
        }

        boolean isClass = (bean instanceof Class);

        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<Object> beanClass = isClass ? (Class<Object>) bean : (Class<Object>) bean.getClass();

        return beanClass.getAnnotation(Table.class) != null;
    }


    public static String getTableName(Object bean) {

        if (bean == null) {
            return null;
        }


        boolean isClass = (bean instanceof Class);

        @SuppressWarnings({"unchecked", "rawtypes"})
        Class<Object> beanClass = isClass ? (Class<Object>) bean : (Class<Object>) bean.getClass();

        Table tableAnn = beanClass.getAnnotation(Table.class);

        if (tableAnn != null && !StringUtils.isBlank(tableAnn.value())) {
            return tableAnn.value();
        } else {
            return beanClass.getSimpleName();
        }

    }

    public static List<ColumnMeta> getColInfoInAttrNames(Object bean, boolean isAllAttr, String[] attrNames, String[] skipAttrNames, boolean isSilently) throws DaoException {

        List<ColumnMeta> columnMetas = getColInfos(bean);
        if (columnMetas.isEmpty()) {
            if (isSilently) {
                return null;
            } else {
                throw new DaoException("ColInfos is empty");
            }
        }


        if (!CollectionUtil.argsIsBlank(attrNames)) {

            Set<String> attrNameSet = new HashSet<String>();

            for (String attr : attrNames) {
                if ( attr != null ) {
                    attrNameSet.add(attr.toLowerCase());
                }
            }

            if (!isAllAttr) {

                List<ColumnMeta> colInfosTmp = new ArrayList<ColumnMeta>();
                for (ColumnMeta ci : columnMetas) {
                    if (attrNameSet.contains(ci.attrName.toLowerCase())) {
                        colInfosTmp.add(ci);
                    }

                }

                if (colInfosTmp.isEmpty()) {
                    if (isSilently) {
                        return null;
                    } else {
                        throw new DaoException("ColInfos in attrNames is empty");
                    }
                } else {
                    columnMetas = colInfosTmp;
                }

            }

        }


        if (!CollectionUtil.argsIsBlank(skipAttrNames)) {

            Set<String> skipAttrNameSet = new HashSet<String>();
            for ( String attr : skipAttrNames ) {
                if ( attr != null ) {
                    skipAttrNameSet.add(attr.toLowerCase());
                }
            }

            List<ColumnMeta> skipColumnMetas = new ArrayList<ColumnMeta>();
            for (ColumnMeta ci : columnMetas) {
                if (!skipAttrNameSet.contains(ci.attrName.toLowerCase())) {
                    skipColumnMetas.add(ci);
                }
            }

            columnMetas = skipColumnMetas;

        }

        log.debug("colInfos ===== {}", columnMetas);

        return columnMetas;

    }

    public static List<ColumnMeta> getColInfos(Object bean) {

        boolean isClass = (bean instanceof Class);

        @SuppressWarnings("rawtypes")
        Class beanClass = isClass ? (Class) bean : bean.getClass();

        List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();

        Field[] fs = h2o.jodd.util.ClassUtil.getSupportedFields(beanClass);
        for (Field f : fs) {

            Column colAnn = f.getAnnotation(Column.class);
            if (colAnn == null) {
                continue;
            }

            ColumnMetaBuilder ci = new ColumnMetaBuilder();

            String fieldName = f.getName();

            ci.attrName = colAnn.attrName();
            if (StringUtils.isBlank(ci.attrName)) {
                ci.attrName = fieldName;
            }

            String colName = colAnn.value();
            ci.colName = StringUtils.isBlank(colName) ? fieldName : colName;

            PK id = f.getAnnotation(PK.class);
            if (id != null) {
                ci.pk = true;
            }

            Unique unique = f.getAnnotation(Unique.class);
            if (unique != null) {
                ci.uniqueNames = unique.value();
            }

            columnMetas.add(new ColumnMeta(fieldName, ci));
        }

        return columnMetas;
    }


}
