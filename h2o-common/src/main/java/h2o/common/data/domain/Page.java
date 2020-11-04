package h2o.common.data.domain;

import java.util.List;

public class Page<T> implements Pageable , java.io.Serializable {

	private static final long serialVersionUID = -4017120973784492101L;

	private long pageNo;
	private long pageSize;

	private long totalPages;
	private long totalElements;

	private List<T> content;

	public Page() {}

	public Page( Pageable pageInfo , List<T> content) {

		this.pageNo = pageInfo.getPageNo();
		this.pageSize = pageInfo.getPageSize();
		this.totalPages = pageInfo.getTotalPages();
		this.totalElements = pageInfo.getTotalElements();

	    this.content = content;
    }

	public void setPageInfo( Pageable pageInfo ) {
		this.pageNo = pageInfo.getPageNo();
		this.pageSize = pageInfo.getPageSize();
		this.totalPages = pageInfo.getTotalPages();
		this.totalElements = pageInfo.getTotalElements();
	}


	@Override
	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	@Override
	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	@Override
	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Page{");
		sb.append("pageNo=").append(pageNo);
		sb.append(", pageSize=").append(pageSize);
		sb.append(", totalPages=").append(totalPages);
		sb.append(", totalElements=").append(totalElements);
		sb.append(", content=").append(content);
		sb.append('}');
		return sb.toString();
	}
}
