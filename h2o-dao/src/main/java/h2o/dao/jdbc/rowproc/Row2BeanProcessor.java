package h2o.dao.jdbc.rowproc;

public interface Row2BeanProcessor<T> {
	public T toBean(Object row) ;
}
