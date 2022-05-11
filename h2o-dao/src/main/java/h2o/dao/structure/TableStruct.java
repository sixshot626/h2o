package h2o.dao.structure;

import h2o.common.collection.IgnoreCaseMap;
import h2o.common.lang.K;
import h2o.common.util.collection.ListBuilder;
import h2o.common.util.lang.ArgsUtil;

import java.util.*;

public class TableStruct {

    private final String tableName;
    private final List<ColumnMeta> columns;
    private final List<ColumnMeta> ids;


    private final Map<String, ColumnMeta> nameMap;
    private final Map<String, ColumnMeta> attrMap;


    TableStruct(String tableName, List<ColumnMeta> columns, List<ColumnMeta> ids) {
        this.tableName = tableName;
        {
            List<ColumnMeta> cols = new ArrayList<>();
            cols.addAll(columns);
            this.columns = Collections.unmodifiableList(cols);
        }
        {
            List<ColumnMeta> cols = new ArrayList<>();
            cols.addAll(ids);
            this.ids = Collections.unmodifiableList(cols);
        }

        {
            Map<String, ColumnMeta> nMap = new IgnoreCaseMap<>(new HashMap<>(), IgnoreCaseMap.LOWER);
            Map<String, ColumnMeta> aMap = new IgnoreCaseMap<>(new HashMap<>(), IgnoreCaseMap.LOWER);
            for (ColumnMeta col : this.columns()) {
                nMap.put(col.name, col);
                aMap.put(col.attrName, col);
            }
            this.nameMap = Collections.unmodifiableMap(nMap);
            this.attrMap = Collections.unmodifiableMap(aMap);
        }
    }


    private TableStruct(String tableName, List<ColumnMeta> columns, List<ColumnMeta> ids,
                        Map<String, ColumnMeta> nameMap, Map<String, ColumnMeta> attrMap) {
        this.tableName = tableName;
        this.columns = columns;
        this.ids = ids;
        this.nameMap = nameMap;
        this.attrMap = attrMap;
    }

    public TableStruct newTableName(String tableName) {
        return new TableStruct(tableName , this.columns , this.ids , this.nameMap , this.attrMap);
    }

    public String tableName() {
        return tableName;
    }

    public List<ColumnMeta> pk() {
        return ids;
    }

    public List<ColumnMeta> listColumnsByUniqueName(String uniqueName) {

        List<ColumnMeta> u = ListBuilder.newList();

        for (ColumnMeta ci : columns) {
            if (ci.uniqueNames != null &&
                    Arrays.asList(ci.uniqueNames).stream()
                            .anyMatch(colUniName -> colUniName.equalsIgnoreCase(uniqueName))) {
                u.add(ci);
            }
        }

        return u;

    }


    public ColumnMeta findColumn(Object k) {

        ColumnMeta col;
        if (k instanceof Enum) {
            col = nameMap.get(((Enum<?>) k).name());
        } else if (k instanceof K) {
            col = attrMap.get(((K) k).name());
        } else if (k instanceof String) {
            col = attrMap.get(k);
        } else {
            throw new IllegalArgumentException(String.valueOf(k));
        }

        return col;
    }

    public ColumnMeta getColumn(Object k) {

        ColumnMeta col = findColumn(k);

        if (col == null) {
            throw new NoSuchElementException(String.valueOf(k));
        }

        return col;
    }

    public List<ColumnMeta> filterColumns(Object attrName, Object... more) {
        return filterColumns(ArgsUtil.more2List(attrName, more));
    }

    public List<ColumnMeta> filterColumns(Collection<?> attrNames) {

        List<ColumnMeta> cis = ListBuilder.newList();
        for (Object attr : attrNames) {
            ColumnMeta cm = findColumn(attr);
            if (cm != null) {
                cis.add(cm);
            }
        }

        return cis;
    }


    public List<ColumnMeta> columns() {
        return columns;
    }


    @Override
    public String toString() {
        return "TableStruct{" +
                "tableName='" + tableName + '\'' +
                ", columns=" + columns +
                ", ids=" + ids +
                '}';
    }
}
