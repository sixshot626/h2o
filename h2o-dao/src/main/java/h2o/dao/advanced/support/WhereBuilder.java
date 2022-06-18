package h2o.dao.advanced.support;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.lang.K;
import h2o.common.util.lang.StringUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.structure.TableStruct;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class WhereBuilder implements WhereConditions {

    private int i;

    private boolean conjunction;

    private boolean allowUnconditional;

    private final boolean sub;


    private final StringBuilder sqlBuilder = new StringBuilder();

    private final Map<Object,Object> para = new HashMap<>();

    private final TableStruct tableStruct;

    public WhereBuilder(TableStruct tableStruct) {
        this.tableStruct = tableStruct;
        this.allowUnconditional = true;
        this.sub = false;
    }

    private WhereBuilder(TableStruct tableStruct , int startParaIndex) {
        this.tableStruct = tableStruct;
        this.i = startParaIndex;
        this.allowUnconditional = true;
        this.sub = true;
    }


    public WhereBuilder unconditional(boolean unconditional) {
        this.allowUnconditional = unconditional;
        return this;
    }


    private void addCondition( boolean condition, String op , Object col, Object val ) {

        i++;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            String p = StringUtil.build( "w_" , i , "_" , name(col).toLowerCase() );

            sqlBuilder.append(column(col));
            sqlBuilder.append(" ");
            sqlBuilder.append(op);
            sqlBuilder.append(" :");
            sqlBuilder.append(p);
            sqlBuilder.append(" ");

            this.para.put( p , val );

        }

    }


    private void addCondition( boolean condition, String op , Object col  ) {

        i++;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            sqlBuilder.append(column(col));
            sqlBuilder.append(" ");
            sqlBuilder.append(op);
            sqlBuilder.append(" ");

        }

    }


    public WhereBuilder eq(boolean condition, Object column, Object val) {
        addCondition(condition , "=" , column , val );
        return this;
    }

    public WhereBuilder ne(boolean condition, Object column, Object val) {
        addCondition(condition , "<>" , column , val );
        return this;
    }

    public WhereBuilder gt(boolean condition, Object column, Object val) {
        addCondition(condition , ">" , column , val );
        return this;
    }

    public WhereBuilder gteq(boolean condition, Object column, Object val) {
        addCondition(condition , ">=" , column , val );
        return this;
    }

    public WhereBuilder lt(boolean condition, Object column, Object val) {
        addCondition(condition , "<" , column , val );
        return this;
    }

    public WhereBuilder lteq(boolean condition, Object column, Object val) {
        addCondition(condition , "<=" , column , val );
        return this;
    }

    public WhereBuilder like(boolean condition, Object column, Object val) {
        addCondition(condition , "like" , column , val );
        return this;
    }

    public WhereBuilder notLike(boolean condition, Object column, Object val) {
        addCondition(condition , "not like" , column , val );
        return this;
    }

    public WhereBuilder in(boolean condition, Object column, Object val) {

        i++;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            String p = StringUtil.build( "w_" , i , "_" , name(column).toLowerCase() );

            sqlBuilder.append(column(column));
            sqlBuilder.append(" in ( :");

            sqlBuilder.append(p);
            sqlBuilder.append(" ) ");

            this.para.put( p , val );

        }
        return this;
    }

    public WhereBuilder notIn(boolean condition, Object column, Object val) {

        i++;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            String p = StringUtil.build( "w_" , i , "_" , name(column).toLowerCase() );

            sqlBuilder.append(column(column));
            sqlBuilder.append(" not in ( :");
            sqlBuilder.append(p);
            sqlBuilder.append(" ) ");

            this.para.put( p , val );

        }
        return this;
    }


    public WhereBuilder isNull(boolean condition, Object column) {
        addCondition(condition , "is null" , column );
        return this;
    }

    public WhereBuilder isNotNull(boolean condition, Object column) {
        addCondition(condition , "is not null" , column );
        return this;
    }


    public WhereBuilder and (boolean condition, Consumer<WhereBuilder> consumer) {

        WhereBuilder whereBuilder = new WhereBuilder( this.tableStruct , this.i );
        consumer.accept( whereBuilder );

        String sql = whereBuilder.whereSql();

        this.i = whereBuilder.i;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            sqlBuilder.append(" ( ");
            sqlBuilder.append( sql );
            sqlBuilder.append(" ) ");

            this.para.putAll( whereBuilder.params() );

        }

        return this;
    }

    public WhereBuilder or (boolean condition, Consumer<WhereBuilder> consumer) {

        WhereBuilder whereBuilder = new WhereBuilder( this.tableStruct , this.i );
        consumer.accept( whereBuilder );

        String sql = whereBuilder.whereSql();

        this.i = whereBuilder.i;

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    or  ");
            }

            sqlBuilder.append(" ( ");
            sqlBuilder.append( sql );
            sqlBuilder.append(" ) ");

            this.para.putAll( whereBuilder.params() );

        }

        return this;

    }


    public WhereBuilder or(boolean condition) {

        if ( condition ) {
            if (conjunction) {
                sqlBuilder.append("\n    or  ");
                conjunction = false;
            }
        }

        return this;

    }


    public WhereBuilder str(boolean condition, Object... strs) {

        if ( condition ) {
            if (!conjunction) {
                conjunction = true;
            } else {
                sqlBuilder.append("\n    and ");
            }

            sqlBuilder.append( str( strs ) );

        }

        return this;

    }




    @Override
    public String whereSql() {

        String sql = sqlBuilder.toString();

        if (  StringUtils.isBlank( sql )  ) {
            if ( allowUnconditional  ) {
                return null;
            } else {
                throw new DaoException("'where' is setted");
            }
        }

        if ( sub ) {
            return str(" \n        ",  sql);
        } else {
            return str(" \nwhere   ",  sql);
        }


    }

    @Override
    public Map<Object, Object> params() {
        return para;
    }





    private String str(Object... strs) {
        StringBuilder sb = new StringBuilder();
        for (Object s : strs) {
            if (s != null) {
                sb.append(name(s));
            }
        }
        return sb.toString();
    }

    private String name(Object obj) {
        String key;
        if (obj instanceof String) {
            key = (String) obj;
        } else if (obj instanceof K) {
            key = ((K) obj).name();
        } else if (obj instanceof Enum) {
            key = ((Enum) obj).name();
        } else {
            throw new IllegalArgumentException(String.valueOf(obj));
        }
        return key;
    }


    private String column(Object attr) {
        return this.tableStruct.getColumn(attr).colName;
    }




}
