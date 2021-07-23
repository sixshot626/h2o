package h2o.dao.advanced;

import h2o.common.data.domain.Page;
import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.Val;
import h2o.common.util.lang.GenericsUtil;
import h2o.dao.Dao;
import h2o.dao.DbUtil;
import h2o.dao.column.ColumnMeta;

import java.util.List;

public abstract class BasicRepository<E> {

    private final String dataSourceName;

    private final Dao dao;

    protected BasicRepository() {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = null;
    }

    protected BasicRepository(String dataSourceName) {
        this.dataSourceName = dataSourceName;
        this.dao = null;
    }

    protected BasicRepository(Dao dao) {
        this.dataSourceName = DbUtil.DEFAULT_DATASOURCE_NAME;
        this.dao = dao;
    }




    public void add(E entity) {
        createDaoBasicUtil().add(entity);
    }

    public void batAdd(List<E> entities) {
        createDaoBasicUtil().batAdd(entities);
    }



    public int edit(E entity) {
        return createDaoBasicUtil().edit(entity);
    }

    public int editByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().editByUnique(entity, uniqueName);
    }

    public int editByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().editByAttr(entity, attrName , more );
    }

    public int editWhere( E entity , String where , Object... args  ) {
        return createDaoBasicUtil().editWhere( entity, where , args );
    }


    public int edit( String[] fields , E entity) {
        return createDaoBasicUtil().edit( fields , entity );
    }

    public int editByUnique( String[] fields , E entity, String uniqueName) {
        return createDaoBasicUtil().editByUnique(  fields ,  entity, uniqueName );
    }

    public int editByAttr( String[] fields , E entity, String attrName , String... more ) {
        return createDaoBasicUtil().editByAttr(  fields ,  entity, attrName , more );
    }

    public int editWhere( String[] fields , E entity , String where , Object... args  ) {
        return createDaoBasicUtil().editWhere( fields ,  entity, where , args );
    }



    public int update( E entity  ) {
        return createDaoBasicUtil().update(entity);
    }

    public int updateByUnique(E entity , String uniqueName) {
        return createDaoBasicUtil().updateByUnique(entity, uniqueName);
    }

    public int updateByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().updateByAttr(entity, attrName , more );
    }

    public int updateWhere( E entity , String where , Object... args  ) {
        return createDaoBasicUtil().updateWhere( entity, where , args );
    }



    public int update( String[] fields , E entity  ) {
        return createDaoBasicUtil().update( fields , entity);
    }

    public int updateByUnique( String[] fields , E entity , String uniqueName) {
        return createDaoBasicUtil().updateByUnique( fields , entity, uniqueName);
    }

    public int updateByAttr(String[] fields , E entity, String attrName , String... more ) {
        return createDaoBasicUtil().updateByAttr( fields , entity, attrName , more );
    }


    public int updateWhere( String[] fields , E entity , String where , Object... args  ) {
        return createDaoBasicUtil().updateWhere( fields , entity, where , args );
    }





    public Val<E> get(E entity) {
        return createDaoBasicUtil().get(entity);
    }

    public Val<E> getAndLock(E entity) {
        return createDaoBasicUtil().getAndLock(entity);
    }

    public Val<E> getByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getByUnique(entity, uniqueName);
    }

    public Val<E> getAndLockByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().getAndLockByUnique(entity, uniqueName);
    }

    public Val<E> getByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().getByAttr(entity, attrName , more );
    }

    public Val<E> getAndLockByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().getAndLockByAttr(entity, attrName , more );
    }

    public Val<E> selectOne(String[] fields, E entity) {
        return createDaoBasicUtil().selectOne(fields, entity);
    }

    public Val<E> selectOneAndLock(String[] fields, E entity) {
        return createDaoBasicUtil().selectOneAndLock(fields, entity);
    }

    public Val<E> selectOneByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneByUnique(fields, entity, uniqueName);
    }

    public Val<E> selectOneAndLockByUnique(String[] fields, E entity, String uniqueName) {
        return createDaoBasicUtil().selectOneAndLockByUnique(fields, entity, uniqueName);
    }

    public Val<E> selectOneByAttr(String[] fields, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().selectOneByAttr(fields, entity, attrName , more );
    }

    public Val<E> selectOneAndLockByAttr(String[] fields, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().selectOneAndLockByAttr(fields, entity, attrName , more );
    }

    public List<E> loadByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().loadByAttr(entity, attrName , more );
    }

    public List<E> selectByAttr(String[] fields, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().selectByAttr(fields, entity, attrName , more );
    }

    public List<E> loadByAttr(SortInfo[] sortInfos, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().loadByAttr(sortInfos, entity, attrName, more );
    }

    public List<E> selectByAttr(String[] fields, SortInfo[] sortInfos, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().selectByAttr(fields, sortInfos, entity, attrName , more );
    }

    public List<E> loadAll(SortInfo... sortInfos) {
        return createDaoBasicUtil().loadAll(sortInfos);
    }

    public List<E> selectAll(String[] fields, SortInfo... sortInfos) {
        return createDaoBasicUtil().selectAll(fields, sortInfos);
    }

    public Page<E> pagingLoadByAttr(PageRequest pageRequest, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().pagingLoadByAttr(pageRequest, entity, attrName , more );
    }

    public Page<E> pagingSelectByAttr(String[] fields, PageRequest pageRequest, E entity, String attrName , String... more ) {
        return createDaoBasicUtil().pagingSelectByAttr(fields, pageRequest, entity, attrName , more );
    }

    public Page<E> pagingLoad(PageRequest pageRequest) {
        return createDaoBasicUtil().pagingLoad(pageRequest);
    }

    public Page<E> pagingSelect(String[] fields, PageRequest pageRequest) {
        return createDaoBasicUtil().pagingSelect(fields, pageRequest);
    }

    public int del(E entity) {
        return createDaoBasicUtil().del(entity);
    }

    public int delByUnique(E entity, String uniqueName) {
        return createDaoBasicUtil().delByUnique(entity, uniqueName);
    }

    public int delByAttr(E entity, String attrName , String... more ) {
        return createDaoBasicUtil().delByAttr(entity, attrName , more );
    }


    public int delWhere( E entity , String where , Object... args  ) {
        return createDaoBasicUtil().delWhere( entity , where , args );
    }



    private final Class<E> entityClazz = (Class<E>) GenericsUtil.getSuperClassGenricType(this.getClass());

    protected Class<E> getEntityClass() {
        return this.entityClazz;
    }

    private final EntityParser entityParser = new EntityParser( this.getEntityClass() );

    protected EntityParser getEntityParser() {
        return this.entityParser;
    }

    protected Dao getDao() {
        return dao == null ? DbUtil.getDao( this.dataSourceName) : dao;
    }


    protected DaoBasicUtil<E> createDaoBasicUtil() {
        return new DaoBasicUtil<E>( this.getEntityClass(), this.getEntityParser() , this.getDao() );
    }


    public String tableName() {
        return this.getEntityParser().getTableName();
    }

    public String column( String attrName ) {
        ColumnMeta columnMeta = this.getEntityParser().getColumn(attrName);
        if ( columnMeta == null ) {
            throw new IllegalArgumentException(attrName);
        }
        return columnMeta.colName;
    }


}
