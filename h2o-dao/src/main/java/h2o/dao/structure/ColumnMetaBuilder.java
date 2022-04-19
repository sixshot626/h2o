package h2o.dao.structure;

public class ColumnMetaBuilder {

    String attrName;
    String colName;
    boolean pk;
    String[] uniqueNames;

    public ColumnMetaBuilder() {
    }

    public ColumnMetaBuilder(ColumnMeta ci) {
        this.attrName = ci.attrName;
        this.colName = ci.colName;
        this.pk = ci.pk;
        this.uniqueNames = ci.uniqueNames;
    }

    public ColumnMetaBuilder setAttrName(String attrName) {
        this.attrName = attrName;
        return this;
    }

    public ColumnMetaBuilder setColName(String colName) {
        this.colName = colName;
        return this;
    }

    public ColumnMetaBuilder setPk(boolean pk) {
        this.pk = pk;
        return this;
    }

    public ColumnMetaBuilder setUniqueNames(String[] uniqueNames) {
        this.uniqueNames = uniqueNames;
        return this;
    }


}
