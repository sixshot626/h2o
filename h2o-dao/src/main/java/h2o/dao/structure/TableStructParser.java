package h2o.dao.structure;

import h2o.common.Mode;
import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.util.collection.ListBuilder;

import java.util.List;

public class TableStructParser {

    private static final boolean CACHE = !Mode.isUserMode("DONT_CACHE_ENTITYPARSER");

    private static final InstanceTable<Class<?>, TableStruct> ENTITYPARSER_TABLE =
            new InstanceTable<Class<?>, TableStruct>(new AbstractInstanceFactory<TableStruct>() {

                @Override
                public TableStruct create(Object entityClazz) {
                    return parseTableStruct((Class<?>) entityClazz);
                }

            });


    public static TableStruct parse(Class<?> tableDefClass) {
        return CACHE ? ENTITYPARSER_TABLE.getAndCreateIfAbsent(tableDefClass) :
                parseTableStruct(tableDefClass);
    }


    private static TableStruct parseTableStruct(Class<?> entityClazz) {

        String tableName = ColumnMetaUtil.getTableName(entityClazz);

        List<ColumnMeta> cil = ColumnMetaUtil.getColInfos(entityClazz);

        List<ColumnMeta> idl = ListBuilder.newList();

        for (ColumnMeta ci : cil) {
            if (ci.pk) {
                idl.add(ci);
            }
        }

        return new TableStruct(tableName, cil, idl);

    }

}
