package h2o.dao.advanced;

import h2o.common.collections.CollectionUtil;

import java.util.List;

public class BasicRepository<E> {

    public void add(E entity) {
        this.createDaoBasicUtil(entity).add(entity);
    }

    public void batAdd(List<E> entities) {
        if (CollectionUtil.isNotBlank(entities)) {
            this.createDaoBasicUtil(entities.get(0)).batAdd(entities);
        }
    }

    public int edit(E entity) {
        return this.createDaoBasicUtil(entity).edit(entity);
    }

    public int editByUnique(E entity, String uniqueName) {
        return this.createDaoBasicUtil(entity).editByUnique(entity, uniqueName);
    }

    public int editByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).editByAttr(entity, attrNames);
    }

    public E get(E entity) {
        return this.createDaoBasicUtil(entity).get(entity);
    }

    public E getAndLock(E entity) {
        return this.createDaoBasicUtil(entity).getAndLock(entity);
    }

    protected E get(E entity, boolean lock) {
        return this.createDaoBasicUtil(entity).get(entity, lock);
    }


    public E getByUnique( E entity, String uniqueName ) {
        return this.getByUnique( entity , false ,uniqueName );
    }

    public E getAndLockByUnique( E entity, String uniqueName ) {
        return this.getByUnique( entity , true ,uniqueName );
    }

    protected E getByUnique(E entity, boolean lock, String uniqueName ) {
        return this.createDaoBasicUtil(entity).getByUnique(entity,lock ,uniqueName );
    }


    public E getByAttr(E entity ,  String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, false , attrNames );
    }

    public E getAndLockByAttr(E entity , String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, true  , attrNames );
    }


    protected E getByAttr(E entity , boolean lock , String... attrNames) {
        return this.createDaoBasicUtil(entity).getByAttr(entity, lock, attrNames );
    }

    public List<E> loadByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).loadByAttr(entity, attrNames);
    }

    public List<E> loadAll( Class<E> entityClazz) {
        return this.createDaoBasicUtil(entityClazz).loadAll();
    }


    public int del(E entity) {
        return this.createDaoBasicUtil(entity).del(entity);
    }

    public int delByUnique(E entity, String uniqueName) {
        return this.createDaoBasicUtil(entity).delByUnique(entity, uniqueName);
    }

    public int delByAttr(E entity, String... attrNames) {
        return this.createDaoBasicUtil(entity).delByAttr(entity, attrNames);
    }

    protected DaoBasicUtil<E> createDaoBasicUtil( E entity ) {
        return new DaoBasicUtil<E>(entity.getClass());
    }

    protected DaoBasicUtil<E> createDaoBasicUtil( Class<?> entityClazz ) {
        return new DaoBasicUtil<E>(entityClazz);
    }

}
