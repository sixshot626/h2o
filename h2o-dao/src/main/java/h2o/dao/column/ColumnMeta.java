package h2o.dao.column;

import org.apache.commons.lang.builder.ToStringBuilder;

public class ColumnMeta {

	public final String attrName;
	public final String colName;
	public final String defVal;

	public final boolean pk;
	public final String[] uniqueNames;


    public ColumnMeta(ColumnMetaBuilder civ ) {
        this.attrName    = civ.attrName;
        this.colName     = civ.colName;
        this.defVal      = civ.defVal;
        this.pk          = civ.pk;
        this.uniqueNames = civ.uniqueNames;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Object value( boolean isNull ) {
		if( isNull && this.defVal != null ) {
			return this.defVal;
		}
		return ":" + this.attrName;
	}
	
	


	public String getAttrName() {
		return attrName;
	}

	public String getColName() {
		return colName;
	}

	public String getDefVal() {
		return defVal;
	}

    public boolean isPk() {
        return pk;
    }

    public String[] getUniqueNames() {
        return uniqueNames;
    }

}