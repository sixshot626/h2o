package h2o.dao.advanced;

import h2o.common.Mode;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.lang.StringUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.colinfo.ColInfo;

import java.util.List;

/**
 * Created by zhangjianwei on 2017/7/1.
 */
public final class DaoBasicUtil<E> {

    private static boolean CACHE = Mode.isUserMode("DONT_CACHE_ENTITYPARSER") ? false : true;

    private static final InstanceTable<Class<?>,EntityParser> ENTITYPARSER_TABLE =
            new InstanceTable<Class<?>, EntityParser>( new AbstractInstanceFactory<EntityParser>() {

                @Override
                public EntityParser create( Object entityClazz ) {
                    return new EntityParser( (Class<?>) entityClazz );
                }

            });

    private final Dao dao;
    private final EntityParser entityParser;


    private final Class<E> entityClazz;

    public DaoBasicUtil( Class<?> entityClazz ) {
        this( entityClazz , DbUtil.getDao() );
    }

    public DaoBasicUtil( Class<?> entityClazz , Dao dao ) {

        this.entityClazz = (Class<E> )entityClazz;
        this.dao = dao;
        this.entityParser = CACHE ? ENTITYPARSER_TABLE.getAndCreateIfAbsent(entityClazz) :
                new EntityParser( entityClazz );
    }

    public String tableName() {
        return this.entityParser.getTableName();
    }


    public void add( E entity ) {
        dao.update( DbUtil.sqlBuilder.buildInsertSql(entity) , entity );
    }

    public void batAdd( List<E> entities ) {
        dao.batchUpdate( DbUtil.sqlBuilder.buildInsertSqlIncludeNull( entities.get(0) ) , entities );
    }



    public int edit( E entity ) {
        return editByColInfos( entity , checkAndGetPk() );
    }

    public int editByUnique( E entity , String uniqueName ) {
        return editByColInfos( entity , checkAndGetUnique(uniqueName) );
    }

    public int editByAttr( E entity , String... attrNames  ) {
        return editByColInfos( entity , checkAndGetAttrs(attrNames) );
    }

    private int editByColInfos( E entity , List<ColInfo> cis ) {

        List<String> ks = ListBuilder.newList();
        for ( ColInfo ci : cis ) {
            ks.add( ci.attrName );
        }

        return dao.update( DbUtil.sqlBuilder.buildUpdateSql3(
                entity , buildWhereStr( cis )  , (String[])ks.toArray( new String[ks.size() ] )
                ) , entity );
    }





    public E get( E entity ) {
        return this.get( entity , false );
    }

    public E getAndLock( E entity ) {
        return this.get( entity , true );
    }

    public E get( E entity  , boolean lock ) {
        return getByColInfos( entity , checkAndGetPk() , lock );
    }

    public E getByUnique( E entity , boolean lock , String uniqueName  ) {
        return getByColInfos( entity , checkAndGetUnique(uniqueName) , lock );
    }

    public E getByAttr( E entity , boolean lock , String... attrNames  ) {
        return getByColInfos( entity , checkAndGetAttrs(attrNames) , lock );
    }

    private E getByColInfos( E entity , List<ColInfo> cis , boolean lock ) {

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select * from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );
        if( lock ) {
            sql.append(" for update ");
        }

        return (E)dao.get( entity.getClass() , sql.toString() , entity );

    }


    public List<E> loadByAttr( E entity , String... attrNames  ) {

        List<ColInfo> cis = checkAndGetAttrs(attrNames);

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select * from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );

        return (List<E>)dao.load( entity.getClass() , sql.toString() , entity );

    }

    public List<E> loadAll() {
        return (List<E>)dao.load( this.entityClazz , "select * from " + this.entityParser.getTableName() );
    }


    public int del( E entity ) {
        return delByColInfos( entity , checkAndGetPk() );
    }

    public int delByUnique( E entity , String uniqueName ) {
        return delByColInfos( entity , checkAndGetUnique(uniqueName) );
    }

    public int delByAttr( E entity , String... attrNames  ) {
        return delByColInfos( entity , checkAndGetAttrs(attrNames) );
    }

    private int delByColInfos( E entity , List<ColInfo> cis  ) {
        return dao.update( "delete from " + this.entityParser.getTableName() +  " where " +  buildWhereStr( cis ) , entity );
    }




    private List<ColInfo> checkAndGetPk() {

        List<ColInfo> cis = this.entityParser.getPK();
        Assert.notEmpty( cis , "Primary key not defined" );

        return cis;
    }

    private List<ColInfo> checkAndGetUnique( String uniqueName ) {

        List<ColInfo> cis = this.entityParser.getUnique( uniqueName );
        Assert.notEmpty( cis , "The unique constraint '" + uniqueName + "' is undefined" );

        return cis;
    }

    private List<ColInfo> checkAndGetAttrs( String[] attrNames  ) {

        List<ColInfo> cis = this.entityParser.getAttrs( attrNames );
        Assert.notEmpty( cis , "Column is undefined" );

        return cis;
    }






    private String buildWhereStr(List<ColInfo> wColInfos  ) {

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for ( ColInfo ci : wColInfos ) {
            if (  i++ > 0 ) {
                sb.append( " and ");
            }
            sb.append( ci.colName );
            sb.append( " = :");
            sb.append( ci.attrName );
        }
        sb.append( ' ' );
        return sb.toString();

    }
}
