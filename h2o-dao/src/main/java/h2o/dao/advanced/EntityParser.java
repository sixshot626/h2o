package h2o.dao.advanced;

import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.dao.colinfo.ColInfo;
import h2o.dao.colinfo.ColInfoUtil;

import java.util.Arrays;
import java.util.List;

public class EntityParser {

    private final ColInfo[] colInfos;

    private final String tableName;

    private final ColInfo[] ids;


    public EntityParser( Class<?> entityClazz ) {

        {
            List<ColInfo> cil = ColInfoUtil.getColInfos(entityClazz);
            this.colInfos = cil.toArray(new ColInfo[cil.size()]);
        }

        this.tableName = ColInfoUtil.getTableName( entityClazz );

        {
            List<ColInfo> idl = ListBuilder.newList();

            for (ColInfo ci : colInfos) {
                if (ci.pk) {
                    idl.add(ci);
                }
            }

            ids = idl.toArray( new ColInfo[idl.size()] );
        }

    }

    public String getTableName() {
        return tableName;
    }

    public List<ColInfo> getPK() {
        return ListBuilder.newList( ids );
    }

    public List<ColInfo> getUnique( String uniqueName ) {

        List<ColInfo> u = ListBuilder.newList();

        for ( ColInfo ci : colInfos ) {
            if ( ci.uniqueNames != null && Arrays.asList(ci.uniqueNames).contains( uniqueName ) ) {
                u.add(ci);
            }
        }


        return u;

    }

    public List<ColInfo> getAttrs( String... attrNames ) {

        Assert.isTrue( !CollectionUtil.argsIsBlank(attrNames) );

        List<ColInfo> cis = ListBuilder.newList();
        List<String> ans = Arrays.asList( attrNames );

        for ( ColInfo ci : colInfos ) {
            if ( ans.contains( ci.attrName ) ) {
                cis.add(ci);
            }
        }

        return cis;

    }


    public List<ColInfo> getAllAttrs() {

        List<ColInfo> cis = ListBuilder.newList();

        for ( ColInfo ci : colInfos ) {
             cis.add(ci);
        }

        return cis;

    }


}
