package h2o.common.data.domain;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class Page<T> implements java.io.Serializable {

	private static final long serialVersionUID = -4017120973784492101L;

	private Pageable pageInfo;

	private List<T> records;

	public Page() {}

	public Page( Pageable pageInfo , List<T> records ) {
	    this.pageInfo = pageInfo;
	    this.records = records;
    }


	public Pageable getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Pageable pageInfo) {
		this.pageInfo = pageInfo;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
