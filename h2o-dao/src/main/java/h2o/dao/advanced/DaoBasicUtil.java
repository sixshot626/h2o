package h2o.dao.advanced;

import h2o.common.Mode;
import h2o.common.concurrent.factory.AbstractInstanceFactory;
import h2o.common.concurrent.factory.InstanceTable;
import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.Val;
import h2o.common.thirdparty.spring.util.Assert;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;
import h2o.common.util.collection.MapBuilder;
import h2o.common.util.lang.StringUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.column.ColumnMeta;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangjianwei on 2017/7/1.
 */
public final class DaoBasicUtil<E> {

    private static final boolean CACHE = !Mode.isUserMode("DONT_CACHE_ENTITYPARSER");

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

    public DaoBasicUtil( Class<E> entityClazz, EntityParser entityParser, Dao dao ) {
        this.entityClazz = entityClazz;
        this.dao = dao;
        this.entityParser = entityParser;
    }




    public Class<E> getEntityClass() {
        return entityClazz;
    }

    public EntityParser getEntityParser() {
        return entityParser;
    }

    public Dao getDao() {
        return dao;
    }

    public String tableName() {
        return this.entityParser.getTableName();
    }





    public void add( E entity ) {
        dao.update( DbUtil.sqlBuilder.buildInsertSql(entity , null , null ) , entity );
    }

    public void batAdd( List<E> entities ) {
        dao.batchUpdate( DbUtil.sqlBuilder.buildInsertSqlIncludeNull( entities.get(0) , null , null ) , entities );
    }



    public int edit( E entity ) {
        return updateByColInfos( false , (String[])null , entity ,  checkAndGetPk() );
    }

    public int editByUnique( E entity , String uniqueName ) {
        return updateByColInfos( false , (String[])null , entity , checkAndGetUnique(uniqueName) );
    }

    public int editByAttr( E entity , String... attrNames  ) {
        return updateByColInfos( false , (String[])null , entity , checkAndGetAttrs(attrNames) );
    }

    public int editWhere( E entity , String where , Object... args  ) {
        return updateWhere( false , (String[])null , entity ,  where , args );
    }



    public int edit( String[] fields , E entity ) {
        return updateByColInfos( false , fields , entity  , checkAndGetPk() );
    }

    public int editByUnique( String[] fields , E entity , String uniqueName ) {
        return updateByColInfos( false , fields , entity , checkAndGetUnique(uniqueName) );
    }

    public int editByAttr( String[] fields , E entity , String... attrNames  ) {
        return updateByColInfos( false , fields , entity ,  checkAndGetAttrs(attrNames) );
    }

    public int editWhere( String[] fields , E entity , String where , Object... args  ) {
        return updateWhere( false , fields , entity ,  where , args );
    }



    public int update(  E entity ) {
        return updateByColInfos( true , (String[])null , entity ,  checkAndGetPk() );
    }

    public int updateByUnique(  E entity  ,  String uniqueName ) {
        return updateByColInfos( true , (String[])null , entity ,  checkAndGetUnique(uniqueName) );
    }

    public int updateByAttr(  E entity ,  String... attrNames  ) {
        return updateByColInfos( true , (String[])null , entity  , checkAndGetAttrs(attrNames) );
    }

    public int updateWhere(  E entity ,  String where  , Object... args  ) {
        return updateWhere( true , (String[])null , entity  , where  , args );
    }



    public int update( String[] fields , E entity ) {
        return updateByColInfos( true , fields , entity ,  checkAndGetPk() );
    }

    public int updateByUnique( String[] fields , E entity  ,  String uniqueName ) {
        return updateByColInfos( true , fields , entity ,  checkAndGetUnique(uniqueName) );
    }

    public int updateByAttr( String[] fields , E entity ,  String... attrNames  ) {
        return updateByColInfos( true , fields , entity  , checkAndGetAttrs(attrNames) );
    }


    public int updateWhere( String[] fields , E entity ,  String where  , Object... args  ) {
        return updateWhere( true , fields , entity  , where  , args );
    }




    private int updateByColInfos( boolean includeNull , String[] fields , E entity , List<ColumnMeta> cis ) {

        List<String> ks = ListBuilder.newList();
        for ( ColumnMeta ci : cis ) {
            ks.add( ci.attrName );
        }

        return dao.update( DbUtil.sqlBuilder.buildUpdateSql( includeNull , false ,
                entity , buildWhereStr( cis )  , fields ,  (String[])ks.toArray( new String[ks.size() ] )
        ) , entity );
    }


    private int updateWhere(  boolean includeNull , String[] fields , E entity ,  String where  , Object... args  ) {

        if ( CollectionUtil.argsIsBlank( args ) ) {
            args = new Object[0];
        }

        Object[] sqlArgs = new Object[ args.length + 1 ];
        sqlArgs[0] = entity;
        if ( args.length > 0 ) {
            System.arraycopy(args, 0, sqlArgs, 1, args.length);
        }

        return dao.update( DbUtil.sqlBuilder.buildUpdateSql( includeNull , false , entity , where , fields , null ) , sqlArgs );

    }






    public Val<E> get(E entity ) {
        return getByColInfos( entity , checkAndGetPk() , false );
    }

    public Val<E> getAndLock( E entity ) {
        return getByColInfos( entity , checkAndGetPk() , true );
    }


    public Val<E> getByUnique( E entity , String uniqueName  ) {
        return getByColInfos( entity , checkAndGetUnique(uniqueName) , false );
    }

    public Val<E> getAndLockByUnique( E entity ,  String uniqueName  ) {
        return getByColInfos( entity , checkAndGetUnique(uniqueName) , true );
    }


    public Val<E> getByAttr( E entity ,  String... attrNames  ) {
        return getByColInfos( entity , checkAndGetAttrs(attrNames) , false );
    }

    public Val<E> getAndLockByAttr( E entity , String... attrNames  ) {
        return getByColInfos( entity , checkAndGetAttrs(attrNames) , true );
    }

    private Val<E> getByColInfos(E entity , List<ColumnMeta> cis , boolean lock  ){
        return selectOneByColInfos( null , entity , cis , lock );
    }



    public Val<E> selectOne( String[] fields , E entity  ) {
        return selectOneByColInfos( fields , entity , checkAndGetPk() , false );
    }

    public Val<E> selectOneAndLock( String[] fields , E entity ) {
        return selectOneByColInfos( fields , entity , checkAndGetPk() , true );
    }

    public Val<E> selectOneByUnique( String[] fields , E entity , String uniqueName  ) {
        return selectOneByColInfos( fields , entity , checkAndGetUnique(uniqueName) , false );
    }

    public Val<E> selectOneAndLockByUnique( String[] fields , E entity ,  String uniqueName  ) {
        return selectOneByColInfos( fields , entity , checkAndGetUnique(uniqueName) , true );
    }

    public Val<E> selectOneByAttr( String[] fields , E entity , String... attrNames  ) {
        return selectOneByColInfos( fields , entity , checkAndGetAttrs(attrNames) , false );
    }

    public Val<E> selectOneAndLockByAttr( String[] fields , E entity , String... attrNames  ) {
        return selectOneByColInfos( fields , entity , checkAndGetAttrs(attrNames) , true );
    }

    private Val<E> selectOneByColInfos(String[] fields , E entity , List<ColumnMeta> cis , boolean lock  ) {

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select " , this.connectSelectFileds( fields )  ,
                " from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );
        if( lock ) {
            sql.append(" for update ");
        }

        return (Val<E>) dao.get( entity.getClass() , sql.toString() , entity );

    }



    public List<E> loadByAttr( E entity , String... attrNames  ) {
        return selectByAttr( null , null , entity , attrNames );
    }


    public List<E> selectByAttr( String[] fields  , E entity  , String... attrNames  ) {
        return selectByAttr( fields , null, entity , attrNames  );
    }

    public List<E> loadByAttr( SortInfo[] sortInfos ,  E entity , String... attrNames ) {
        return selectByAttr( null , sortInfos , entity , attrNames );
    }

    public List<E> selectByAttr( String[] fields , SortInfo[] sortInfos ,  E entity  , String... attrNames  ) {

        List<ColumnMeta> cis = checkAndGetAttrs(attrNames);

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


    public Page<E> pagingSelectByAttr( String[] fields , PageRequest pageRequest , E entity , String... attrNames  ) {

        List<ColumnMeta> cis = checkAndGetAttrs(attrNames);

        StringBuilder sql = new StringBuilder();

        StringUtil.append( sql , "select " , this.connectSelectFileds( fields ) ,
                " from " , this.entityParser.getTableName() ,  " where " , buildWhereStr( cis )   );

        return (Page<E>)dao.pagingLoad( entity.getClass() , sql.toString() ,
                new PageRequest( pageRequest.getPageNo() , pageRequest.getPageSize() ,
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
                new PageRequest( pageRequest.getPageNo() , pageRequest.getPageSize() ,
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
        for ( ColumnMeta columnMeta : entityParser.listAllColumns() ) {
            if ( sortInfoMap.containsKey( columnMeta.attrName ) ) {
                attrMap.put( columnMeta.attrName , columnMeta.colName );
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

    private int delByColInfos( E entity , List<ColumnMeta> cis  ) {
        return dao.update( "delete from " + this.entityParser.getTableName() +
                " where " +  buildWhereStr( cis ) , entity );
    }


    public int delWhere( E entity , String where , Object... args  ) {

        if ( CollectionUtil.argsIsBlank( args ) ) {
            args = new Object[0];
        }

        Object[] sqlArgs = new Object[ args.length + 1 ];
        sqlArgs[0] = entity;
        if ( args.length > 0 ) {
            System.arraycopy(args, 0, sqlArgs, 1, args.length);
        }

        return dao.update( "delete from " + this.entityParser.getTableName() + " where " +  where , sqlArgs );

    }




    private List<ColumnMeta> checkAndGetPk() {

        List<ColumnMeta> cis = this.entityParser.getPK();
        Assert.notEmpty( cis , "Primary key not defined" );

        return cis;
    }

    private List<ColumnMeta> checkAndGetUnique(String uniqueName ) {

        List<ColumnMeta> cis = this.entityParser.listColumnsByUniqueName( uniqueName );
        Assert.notEmpty( cis , "The unique constraint '" + uniqueName + "' is undefined" );

        return cis;
    }

    private List<ColumnMeta> checkAndGetAttrs(String[] attrNames  ) {

        List<ColumnMeta> cis = this.entityParser.listColumns( attrNames );
        Assert.notEmpty( cis , "Column is undefined" );

        return cis;
    }






    private String buildWhereStr( List<ColumnMeta> wColumnMetas) {

        StringBuilder sb = new StringBuilder();
        int i = 0;
        for ( ColumnMeta ci : wColumnMetas) {
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
                this._connectSelectFileds( this.entityParser.listAllColumns() ) :
                this._connectSelectFileds( this.entityParser.listColumns( attrs ) );
    }



    private String _connectSelectFileds( List<ColumnMeta> fs ) {

        StringBuilder sb = new StringBuilder();
        sb.append( ' ' );
        int i = 0;
        for ( ColumnMeta ci : fs ) {
            if (  i++ > 0 ) {
                sb.append( " , ");
            }
            sb.append( ci.colName );
        }
        sb.append( ' ' );
        return sb.toString();

    }


}
