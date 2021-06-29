package h2o.dao.advanced;

import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.dao.colinfo.ColInfo;
import h2o.dao.colinfo.ColInfoUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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


    public ColInfo getAttr( String attrName ) {

        for ( ColInfo ci : colInfos ) {
            if ( ci.attrName.equalsIgnoreCase(attrName)  ) {
                return ci;
            }
        }

        return null;
    }

    public List<ColInfo> getAttrs( String... attrNames ) {

        Assert.isTrue( !CollectionUtil.argsIsBlank(attrNames) );

        List<ColInfo> cis = ListBuilder.newList();
        Set<String> ans = new TreeSet<>();
        for ( String attr : attrNames ) {
            ans.add( attr.toUpperCase() );
        }

        for ( ColInfo ci : colInfos ) {
            if ( ans.contains( ci.attrName.toUpperCase() ) ) {
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
