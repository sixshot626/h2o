package h2o.common.dao.rowproc;

public interface Row2BeanProcessor<T> {
	public T toBean(Object row) ;
}
