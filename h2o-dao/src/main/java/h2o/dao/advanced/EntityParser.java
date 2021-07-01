package h2o.dao.advanced;

import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.column.ColumnMeta;
import h2o.dao.column.ColumnMetaUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EntityParser {

    private final ColumnMeta[] columnMetas;

    private final String tableName;

    private final ColumnMeta[] ids;


    public EntityParser( Class<?> entityClazz ) {

        {
            List<ColumnMeta> cil = ColumnMetaUtil.getColInfos(entityClazz);
            this.columnMetas = cil.toArray(new ColumnMeta[cil.size()]);
        }

        this.tableName = ColumnMetaUtil.getTableName( entityClazz );

        {
            List<ColumnMeta> idl = ListBuilder.newList();

            for (ColumnMeta ci : columnMetas) {
                if (ci.pk) {
                    idl.add(ci);
                }
            }

            ids = idl.toArray( new ColumnMeta[idl.size()] );
        }

    }

    public String getTableName() {
        return tableName;
    }

    public List<ColumnMeta> getPK() {
        return ListBuilder.newList( ids );
    }

    public List<ColumnMeta> listColumnByUniqueName(String uniqueName ) {

        List<ColumnMeta> u = ListBuilder.newList();

        for ( ColumnMeta ci : columnMetas) {
            if ( ci.uniqueNames != null &&
                    Arrays.asList(ci.uniqueNames).stream()
                            .anyMatch( colUniName ->  colUniName.equalsIgnoreCase( uniqueName ) ) ) {
                u.add(ci);
            }
        }

        return u;

    }


    public ColumnMeta getColumn(String attrName ) {

        for ( ColumnMeta ci : columnMetas) {
            if ( ci.attrName.equalsIgnoreCase(attrName)  ) {
                return ci;
            }
        }

        return null;
    }

    public List<ColumnMeta> listColumns(String... attrNames ) {

        Assert.isTrue( !CollectionUtil.argsIsBlank(attrNames) );

        List<ColumnMeta> cis = ListBuilder.newList();
        Set<String> ans = new TreeSet<>();
        for ( String attr : attrNames ) {
            ans.add( attr.toUpperCase() );
        }

        for ( ColumnMeta ci : columnMetas) {
            if ( ans.contains( ci.attrName.toUpperCase() ) ) {
                cis.add(ci);
            }
        }

        return cis;

    }


    public List<ColumnMeta> listAllColumns() {

        List<ColumnMeta> cis = ListBuilder.newList();

        for ( ColumnMeta ci : columnMetas) {
             cis.add(ci);
        }

        return cis;

    }


}
