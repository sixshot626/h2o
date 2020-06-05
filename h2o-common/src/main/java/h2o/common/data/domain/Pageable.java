package h2o.common.data.domain;

public interface Pageable {

	long getPageNo();

	long getPageRecordSize();

	long getTotalRecord();

	long getTotalPage();

}
