package h2o.common.data.domain;

public interface Pageable {

	public long getPageNo();

	public long getPageRecordSize();

	public long getTotalRecord();

	public long getTotalPage();

}
