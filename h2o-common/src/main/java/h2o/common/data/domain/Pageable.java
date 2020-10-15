package h2o.common.data.domain;

public interface Pageable {

	long getPageNo();

	long getPageSize();

	long getTotalElements();

	long getTotalPages();

}
