package h2o.dao.advanced.support;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.lang.Gate;
import h2o.common.lang.Key;
import h2o.common.util.lang.StringUtil;
import h2o.dao.exception.DaoException;
import h2o.dao.structure.TableStruct;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class WhereBuilder implements WhereConditions {


    private static final String AND = "    and ";
    private static final String OR  = "     or ";


    private Gate<String> conj = new Gate<>(false,AND);



    private boolean allowUnconditional;

    private final boolean sub;

    private final String prefix;


    private final int indentationLevel;


    private int i;



    private final StringBuilder sqlBuilder = new StringBuilder();

    private final Map<Object,Object> para = new HashMap<>();

    private final TableStruct tableStruct;


    public WhereBuilder(TableStruct tableStruct) {
        this(tableStruct,"w");
    }

    public WhereBuilder(TableStruct tableStruct , String prefix) {
        this.tableStruct = tableStruct;
        this.prefix = prefix;
        this.i = 0;

        this.allowUnconditional = true;
        this.sub = false;

        this.indentationLevel = 0;
    }

    private WhereBuilder(TableStruct tableStruct , String prefix , int startParaIndex , int indentationLevel ) {
        this.tableStruct = tableStruct;
        this.prefix = prefix;
        this.i = startParaIndex;
        this.allowUnconditional = true;
        this.sub = true;

        this.indentationLevel = indentationLevel;

    }


    public WhereBuilder unconditional(boolean unconditional) {
        this.allowUnconditional = unconditional;
        return this;
    }



    private WhereBuilder addBracketsCondition( boolean condition, String op , Object col, Object val ) {

        i++;

        if ( condition ) {

            if (!conj.ok) {
                conj = Gate.ok(conj.value);
            } else {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(conj.value);

                conj = Gate.ok(AND);
            }

            String p = StringUtil.build( prefix , "_" , i , "_" , name(col) );

            sqlBuilder.append(column(col));
            sqlBuilder.append(" ");
            sqlBuilder.append(op);
            sqlBuilder.append(" ( :");
            sqlBuilder.append(p);
            sqlBuilder.append(" ) ");

            this.para.put( p , val );

        }

        return this;

    }


    private WhereBuilder addCondition( boolean condition, String op , Object col, Object val ) {

        i++;

        if ( condition ) {

            if (!conj.ok) {
                conj = Gate.ok(conj.value);
            } else {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(conj.value);

                conj = Gate.ok(AND);
            }

            String p = StringUtil.build( prefix , "_" , i , "_" , name(col) );

            sqlBuilder.append(column(col));
            sqlBuilder.append(" ");
            sqlBuilder.append(op);
            sqlBuilder.append(" :");
            sqlBuilder.append(p);
            sqlBuilder.append(" ");

            this.para.put( p , val );

        }

        return this;

    }


    private WhereBuilder addCondition( boolean condition, String op , Object col  ) {

        i++;

        if ( condition ) {

            if (!conj.ok) {
                conj = Gate.ok(conj.value);
            } else {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(conj.value);

                conj = Gate.ok(AND);
            }

            sqlBuilder.append(column(col));
            sqlBuilder.append(" ");
            sqlBuilder.append(op);
            sqlBuilder.append(" ");

        }

        return this;

    }


    public WhereBuilder eq(boolean condition, Object col, Object val) {
        return addCondition(condition , "=" , col , val );
    }

    public WhereBuilder neq(boolean condition, Object col, Object val) {
        return addCondition(condition , "<>" , col , val );
    }

    public WhereBuilder gt(boolean condition, Object col, Object val) {
        return addCondition(condition , ">" , col , val );
    }

    public WhereBuilder gte(boolean condition, Object col, Object val) {
        return addCondition(condition , ">=" , col , val );
    }

    public WhereBuilder lt(boolean condition, Object col, Object val) {
        return addCondition(condition , "<" , col , val );
    }

    public WhereBuilder lte(boolean condition, Object col, Object val) {
        return addCondition(condition , "<=" , col , val );
    }

    public WhereBuilder like(boolean condition, Object col, Object val) {
        return addCondition(condition , "like" , col , val );
    }

    public WhereBuilder notLike(boolean condition, Object col, Object val) {
        return addCondition(condition , "not like" , col , val );
    }


    public WhereBuilder isNull(boolean condition, Object col) {
        return addCondition(condition , "is null" , col );
    }

    public WhereBuilder isNotNull(boolean condition, Object col) {
        return addCondition(condition , "is not null" , col );
    }


    public WhereBuilder in(boolean condition, Object col, Object val) {
        return addBracketsCondition(condition , "in" , col , val );
    }

    public WhereBuilder notIn(boolean condition, Object col, Object val) {
        return addBracketsCondition(condition , "not in" , col , val );
    }





    public WhereBuilder eq(boolean condition, Object col, Supplier<Object> supplier) {
        return this.eq( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder neq(boolean condition, Object col, Supplier<Object> supplier) {
        return this.neq( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder gt(boolean condition, Object col, Supplier<Object> supplier) {
        return this.gt( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder gte(boolean condition, Object col, Supplier<Object> supplier) {
        return this.gte( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder lt(boolean condition, Object col, Supplier<Object> supplier) {
        return this.lt( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder lte(boolean condition, Object col, Supplier<Object> supplier) {
        return this.lte( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder like(boolean condition, Object col, Supplier<Object> supplier) {
        return this.like( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder notLike(boolean condition, Object col, Supplier<Object> supplier) {
        return this.notLike( condition , col , condition ? supplier.get() : null );
    }



    public WhereBuilder in(boolean condition, Object col, Supplier<Object> supplier) {
        return this.in( condition , col , condition ? supplier.get() : null );
    }

    public WhereBuilder notIn(boolean condition, Object col, Supplier<Object> supplier) {
        return this.notIn( condition , col , condition ? supplier.get() : null );
    }



    public WhereBuilder brackets(boolean condition, Consumer<WhereBuilder> consumer) {

        WhereBuilder whereBuilder = new WhereBuilder( this.tableStruct  , this.prefix , this.i , this.indentationLevel + 1 );
        consumer.accept( whereBuilder );

        this.i = whereBuilder.i;

        String sql = whereBuilder.whereSql();

        if ( condition && StringUtils.isNotBlank( sql ) ) {

            conj = Gate.ok(AND);

            sqlBuilder.append("( ");
            sqlBuilder.append( sql );
            sqlBuilder.append('\n');
            sqlBuilder.append(indentation());
            sqlBuilder.append("        ) ");

            this.para.putAll( whereBuilder.params() );

        }

        return this;

    }


    public WhereBuilder and(boolean condition, Consumer<WhereBuilder> consumer) {

        WhereBuilder whereBuilder = new WhereBuilder( this.tableStruct  , this.prefix , this.i , this.indentationLevel + 1 );
        consumer.accept( whereBuilder );

        this.i = whereBuilder.i;

        String sql = whereBuilder.whereSql();

        if ( condition && StringUtils.isNotBlank( sql ) ) {

            if (conj.ok) {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(AND);
            }

            conj = Gate.ok(AND);

            sqlBuilder.append("( ");
            sqlBuilder.append( sql );
            sqlBuilder.append('\n');
            sqlBuilder.append(indentation());
            sqlBuilder.append("        ) ");

            this.para.putAll( whereBuilder.params() );

        }

        return this;
    }


    public WhereBuilder or(boolean condition, Consumer<WhereBuilder> consumer) {

        WhereBuilder whereBuilder = new WhereBuilder( this.tableStruct  , this.prefix , this.i , this.indentationLevel + 1 );
        consumer.accept( whereBuilder );

        this.i = whereBuilder.i;

        String sql = whereBuilder.whereSql();

        if ( condition && StringUtils.isNotBlank( sql ) ) {

            if (conj.ok) {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(OR);
            }

            conj = Gate.ok(AND);

            sqlBuilder.append("( ");
            sqlBuilder.append( sql );
            sqlBuilder.append('\n');
            sqlBuilder.append(indentation());
            sqlBuilder.append("        ) ");

            this.para.putAll( whereBuilder.params() );

        }

        return this;

    }


    public WhereBuilder or(boolean condition) {

        if ( condition ) {
            conj = new Gate<>(conj.ok , OR);
        }

        return this;

    }


    public WhereBuilder str(boolean condition, Object... strs) {

        if ( condition ) {

            if (!conj.ok) {
                conj = Gate.ok(conj.value);
            } else {
                sqlBuilder.append('\n');
                sqlBuilder.append(indentation());
                sqlBuilder.append(conj.value);

                conj = Gate.ok(AND);
            }

            sqlBuilder.append( buildString( strs ) );

        }

        return this;

    }




    public WhereBuilder eq(Object col, Object val) {
        return this.eq(true,col,val);
    }

    public WhereBuilder neq(Object col, Object val) {
        return this.neq(true,col,val);
    }

    public WhereBuilder gt(Object col, Object val) {
        return this.gt(true,col,val);
    }

    public WhereBuilder gte(Object col, Object val) {
        return this.gte(true,col,val);
    }

    public WhereBuilder lt(Object col, Object val) {
        return this.lt(true,col,val);
    }

    public WhereBuilder lte(Object col, Object val) {
        return this.lte(true,col,val);
    }

    public WhereBuilder like(Object col, Object val) {
        return this.like(true,col,val);
    }

    public WhereBuilder notLike(Object col, Object val) {
        return this.notLike(true,col,val);
    }


    public WhereBuilder isNull(Object col) {
        return this.isNull(true,col);
    }

    public WhereBuilder isNotNull(Object col) {
        return this.isNotNull(true,col);
    }


    public WhereBuilder in(Object col, Object val) {
        return this.in(true,col,val);
    }

    public WhereBuilder notIn(Object col, Object val) {
        return this.notIn(true,col,val);
    }


    public WhereBuilder brackets(Consumer<WhereBuilder> consumer) {
        return this.brackets( true , consumer);
    }

    public WhereBuilder and(Consumer<WhereBuilder> consumer) {
        return this.and(true , consumer );
    }

    public WhereBuilder or(Consumer<WhereBuilder> consumer) {
        return this.or(true , consumer);
    }

    public WhereBuilder or() {
        return this.or(true);
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
            return buildString(" \n", indentation() ,"        ",  sql);
        } else {
            return buildString(" \nwhere   ",  sql);
        }


    }

    @Override
    public Map<Object, Object> params() {
        return para;
    }


    @Override
    public String toString() {
        return this.whereSql();
    }











    private String _indentation = null;
    private String indentation() {



        if ( this._indentation == null ) {

            if ( this.indentationLevel == 0 ) {

                this._indentation = "";

            } else {

                StringBuilder indentationBuilder = new StringBuilder();
                for (int i = 0; i < this.indentationLevel; i++) {
                    indentationBuilder.append("      ");
                }

                this._indentation = indentationBuilder.toString();

            }
        }

        return this._indentation;
    }


    private String buildString(Object... strs) {
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
        } else if (obj instanceof Key) {
            key = ((Key) obj).name();
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
