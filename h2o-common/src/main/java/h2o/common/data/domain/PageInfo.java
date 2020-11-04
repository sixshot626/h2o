package h2o.common.data.domain;

import java.io.Serializable;

public class PageInfo implements Pageable, Serializable {

	private static final long serialVersionUID = -9096938113245080992L;

	private long pageNo;
	private long pageSize;

	private long totalPages;
	private long totalElements;


	public PageInfo() {}

	public PageInfo( Pageable pageable ) {
		this.pageNo = pageable.getPageNo();
		this.pageSize = pageable.getPageSize();
		this.totalPages = pageable.getTotalPages();
		this.totalElements = pageable.getTotalElements();
	}

	public PageInfo( long pageNo, long pageSize ) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

    public PageInfo( PageRequest pageRequest ) {
        this.pageNo = pageRequest.getPageNo();
        this.pageSize = pageRequest.getPageSize();
    }

    public PageInfo( PageRequest pageRequest , long totalElements ){
        this( pageRequest );

        this.totalElements = totalElements;
        this.calcTotalPage();
    }

    public PageInfo(long pageNo, long pageSize, long totalElements ) {
		this(pageNo, pageSize);

		this.totalElements = totalElements;
        this.calcTotalPage();
	}


    public void calcTotalPage() {
        this.totalPages = (this.totalElements + this.pageSize - 1) / this.pageSize;
    }


    public long resetReasonablePageNo() {
        if ( this.pageNo > totalPages) {
            this.pageNo = totalPages;
        }
        this.pageNo = pageNo < 1 ? 1L : pageNo;
        return this.pageNo;
    }

	@Override
	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public long getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
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
	public String toString() {
		final StringBuilder sb = new StringBuilder("PageInfo{");
		sb.append("pageNo=").append(pageNo);
		sb.append(", pageSize=").append(pageSize);
		sb.append(", totalPages=").append(totalPages);
		sb.append(", totalElements=").append(totalElements);
		sb.append('}');
		return sb.toString();
	}
}
