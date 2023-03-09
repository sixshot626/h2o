package h2o.dao.structure;

import h2o.common.Mode;
import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.lang.Val;
import h2o.common.util.collection.ListBuilder;

import java.util.List;

public class TableStructParser {

    private TableStructParser() {}

    private static final boolean CACHE = !Mode.isUserMode("DONT_CACHE_ENTITYPARSER");

    private static final InstanceTable<Class<?>, Val<TableStruct>> ENTITYPARSER_TABLE =
            new InstanceTable<>(new AbstractInstanceFactory<Val<TableStruct>>() {

                @Override
                public Val<TableStruct> create(Object entityClazz) {
                    return parseTableStruct((Class<?>) entityClazz);
                }

            });


    public static Val<TableStruct> parse(Class<?> tableDefClass) {
        return CACHE ? ENTITYPARSER_TABLE.getAndCreateIfAbsent(tableDefClass) :
                parseTableStruct(tableDefClass);
    }


    private static Val<TableStruct> parseTableStruct(Class<?> entityClazz) {

        String tableName = ColumnMetaUtil.getTableName(entityClazz);
        if ( tableName == null ) {
            return Val.empty();
        }

        List<ColumnMeta> cil = ColumnMetaUtil.getColInfos(entityClazz);

        List<ColumnMeta> idl = ListBuilder.newList();

        for (ColumnMeta ci : cil) {
            if (ci.pk) {
                idl.add(ci);
            }
        }

        return new Val<>(new TableStruct(tableName, cil, idl));

    }

}
