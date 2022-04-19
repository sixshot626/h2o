package h2o.dao.structure;

import java.util.Arrays;

public class ColumnMeta {

    public final String name;

    public final String attrName;
    public final String colName;
    public final boolean pk;
    public final String[] uniqueNames;


    public ColumnMeta(String name, ColumnMetaBuilder civ) {
        this.name = name;
        this.attrName = civ.attrName;
        this.colName = civ.colName;
        this.pk = civ.pk;
        this.uniqueNames = civ.uniqueNames;
    }

    public Object value() {
        return ":" + this.attrName;
    }

    public String getName() {
        return name;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getColName() {
        return colName;
    }

    public boolean isPk() {
        return pk;
    }

    public String[] getUniqueNames() {
        return uniqueNames;
    }


    @Override
    public String toString() {
        return "ColumnMeta{" +
                "name='" + name + '\'' +
                ", attrName='" + attrName + '\'' +
                ", colName='" + colName + '\'' +
                ", pk=" + pk +
                ", uniqueNames=" + Arrays.toString(uniqueNames) +
                '}';
    }
}