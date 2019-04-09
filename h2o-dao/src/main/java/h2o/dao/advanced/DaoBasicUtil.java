package h2o.dao.advanced;

import h2o.common.Mode;
import h2o.common.bean.page.Page;
import h2o.common.bean.page.PageRequest;
import h2o.common.bean.page.SortInfo;
import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import h2o.common.collections.builder.MapBuilder;
import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.lang.StringUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.colinfo.ColInfo;

import java.util.List;
import java.util.Map;

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

    public EntityParser getEntityParser() {
        return entityParser;
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

    public E get( E entity , boolean lock ) {
        return getByColInfos( entity , checkAndGetPk() , lock );
    }

    public E getByUnique( E entity , boolean lock , String uniqueName  ) {
        return getByColInfos( entity , checkAndGetUnique(uniqueName) , lock );
    }

    public E getByAttr( E entity , boolean lock , String... attrNames  ) {
        return getByColInfos( entity , checkAndGetAttrs(attrNames) , lock );
    }

    private E getByColInfos( E entity , List<ColInfo> cis , boolean lock  ){
        return selectOneByColInfos( null , entity , cis , lock );
    }



    public E selectOne( String[] fields , E entity  ) {
        return this.selectOne( fields , entity , false );
    }

    public E selectOneAndLock( String[] fields , E entity ) {
        return this.selectOne( fields ,  entity , true );
    }

    public E selectOne( String[] fields , E entity ,  boolean lock ) {
        return selectOneByColInfos( fields , entity , checkAndGetPk() , lock );
    }

    public E selectOneByUnique( String[] fields , E entity ,  boolean lock , String uniqueName  ) {
        return selectOneByColInfos( fields , entity , checkAndGetUnique(uniqueName) , lock );
    }

    public E selectOneByAttr( String[] fields , E entity ,  boolean lock , String... attrNames  ) {
        return selectOneByColInfos( fields , entity , checkAndGetAttrs(attrNames) , lock );
    }



    private E selectOneByColInfos( String[] fields , E entity ,  List<ColInfo> cis , boolean lock  ) {

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select " , this.connectSelectFileds( fields )  ,
                " from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );
        if( lock ) {
            sql.append(" for update ");
        }

        return (E)dao.get( entity.getClass() , sql.toString() , entity );

    }


    public List<E> loadByAttr( E entity , String... attrNames  ) {
        return selectByAttr( null , entity , attrNames );
    }


    public List<E> selectByAttr( String[] fields  , E entity  , String... attrNames  ) {
        return selectByAttr( fields , entity , attrNames , (SortInfo[]) null );
    }


    public List<E> loadAll() {
        return selectAll( null );
    }

    public List<E> selectAll( String[] fields ) {
        return selectAll(fields,(SortInfo[]) null);
    }



    public List<E> loadByAttr( E entity , String[] attrNames , SortInfo... sortInfos  ) {
        return selectByAttr( null , entity , attrNames , sortInfos );
    }


    public List<E> selectByAttr( String[] fields  , E entity  , String[] attrNames , SortInfo... sortInfos  ) {

        List<ColInfo> cis = checkAndGetAttrs(attrNames);

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select " , this.connectSelectFileds( fields ) ,
                " from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );

        return (List<E>)dao.load( entity.getClass() , orderProc( sql.toString() , sortInfos ) , entity );

    }


    public List<E> loadAll( SortInfo... sortInfos  ) {
        return selectAll( null , sortInfos );
    }

    public List<E> selectAll( String[] fields , SortInfo... sortInfos ) {

        StringBuilder sql = new StringBuilder();

        StringUtil.append(sql , "select " , this.connectSelectFileds( fields ) ,
                " from " , this.entityParser.getTableName() );

        return (List<E>)dao.load( this.entityClazz , orderProc( sql.toString() , sortInfos ) );

    }



    private String orderProc( String sql, SortInfo... strts ) {

        if (CollectionUtil.argsIsBlank(strts)) {
            return sql;
        }

        List<SortInfo> strtList = convertSorts(ListBuilder.newList(strts));

        StringBuilder orderSql = new StringBuilder();
        orderSql.append( sql );
        orderSql.append( " order by " );
        int i = 0;
        for (SortInfo sortInfo : strtList) {
            if (i++ > 0) {
                orderSql.append(",");
            }
            orderSql.append(sortInfo.toSqlString());
        }

        return orderSql.toString();

    }




    public Page<E> pagingLoadByAttr( PageRequest pageRequest , E entity , String... attrNames  ) {
        return pagingSelectByAttr( null , pageRequest , entity , attrNames );
    }


    public Page<E> pagingSelectByAttr(String[] fields , PageRequest pageRequest , E entity  , String... attrNames  ) {

        List<ColInfo> cis = checkAndGetAttrs(attrNames);

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select " , this.connectSelectFileds( fields ) ,
                " from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );

        return (Page<E>)dao.pagingLoad( entity.getClass() , sql.toString() ,
                new PageRequest( pageRequest.getPageNo() , pageRequest.getPageRecordSize() ,
                        convertSorts(pageRequest.getSorts()) )  , entity );

    }


    public Page<E> pagingLoad( PageRequest pageRequest ) {
        return pagingSelect( null , pageRequest );
    }

    public Page<E> pagingSelect( String[] fields , PageRequest pageRequest ) {

        StringBuilder sql = new StringBuilder();

        StringUtil.append(sql , "select " , this.connectSelectFileds( fields ) ,
                " from " , this.entityParser.getTableName() );

        return dao.pagingLoad( this.entityClazz , sql.toString() ,
                new PageRequest( pageRequest.getPageNo() , pageRequest.getPageRecordSize() ,
                        convertSorts(pageRequest.getSorts()) ) );

    }


    private List<SortInfo> convertSorts(List<SortInfo> sortInfos  ) {

        if ( CollectionUtil.isBlank( sortInfos ) ) {
            return sortInfos;
        }

        int size = sortInfos.size();

        Map<String,SortInfo> sortInfoMap = MapBuilder.newMap(size);
        for ( SortInfo sortInfo : sortInfos ) {
            sortInfoMap.put( sortInfo.getName() , sortInfo );
        }

        Map<String,String> attrMap = MapBuilder.newMap( size );
        for ( ColInfo colInfo : entityParser.getAllAttrs() ) {
            if ( sortInfoMap.containsKey( colInfo.attrName ) ) {
                attrMap.put( colInfo.attrName , colInfo.colName );
            }
        }

        List<SortInfo> newSortInfos = ListBuilder.newList( size );
        for ( SortInfo sortInfo : sortInfos ) {

            String colName = attrMap.containsKey( sortInfo.getName() ) ?
                    attrMap.get( sortInfo.getName() ) : sortInfo.getName();

            newSortInfos.add( new SortInfo( colName , sortInfo.getDirection() ) );

        }

        return newSortInfos;

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
        return dao.update( "delete from " + this.entityParser.getTableName() +
                " where " +  buildWhereStr( cis ) , entity );
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






    private String buildWhereStr( List<ColInfo> wColInfos  ) {

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



    private String connectSelectFileds( String... attrs ) {
        return CollectionUtil.argsIsBlank( attrs ) ?
                this._connectSelectFileds( this.entityParser.getAllAttrs() ) :
                this._connectSelectFileds( this.entityParser.getAttrs( attrs ) );
    }



    private String _connectSelectFileds( List<ColInfo> fs ) {

        StringBuilder sb = new StringBuilder();
        sb.append( ' ' );
        int i = 0;
        for ( ColInfo ci : fs ) {
            if (  i++ > 0 ) {
                sb.append( " , ");
            }
            sb.append( ci.colName );
        }
        sb.append( ' ' );
        return sb.toString();

    }

}
