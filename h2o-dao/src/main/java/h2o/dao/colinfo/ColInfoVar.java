package h2o.dao.colinfo;

public class ColInfoVar {

    String attrName;
    String colName;
    String defVal;

    boolean pk;
    String[] uniqueNames;

    public ColInfoVar() {
    }

    public ColInfoVar( ColInfo ci ) {
        this.attrName    = ci.attrName;
        this.colName     = ci.colName;
        this.defVal      = ci.defVal;
        this.pk          = ci.pk;
        this.uniqueNames = ci.uniqueNames;
    }

    public ColInfoVar setAttrName(String attrName) {
        this.attrName = attrName;
        return this;
    }

    public ColInfoVar setColName(String colName) {
        this.colName = colName;
        return this;
    }

    public ColInfoVar setDefVal( String defVal ) {
        this.defVal = defVal;
        return this;
    }

    public ColInfoVar setPk( boolean pk) {
        this.pk = pk;
        return this;
    }

    public ColInfoVar setUniqueNames( String[] uniqueNames) {
        this.uniqueNames = uniqueNames;
        return this;
    }

    public ColInfo get() {
        return new ColInfo(this);
    }
}
