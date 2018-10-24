package h2o.common.bean.page;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class PageInfo implements Pageable, Serializable {

	private static final long serialVersionUID = -9096938113245080992L;

	private long totalRecord;
	private long totalPage;

	private long pageNo;
	private long pageRecordSize;

	public PageInfo() {}

	public PageInfo(long pageNo, long pageRecordSize) {

		this.pageNo = pageNo;
		this.pageRecordSize = pageRecordSize;

	}

    public PageInfo( PageRequest pageRequest ) {
        this.pageNo = pageRequest.getPageNo();
        this.pageRecordSize = pageRequest.getPageRecordSize();
    }

    public PageInfo( PageRequest pageRequest , long totalRecord ){

        this( pageRequest );

        this.totalRecord = totalRecord;

        this.calcTotalPage();
    }

    public PageInfo(long pageNo, long pageRecordSize, long totalRecord) {

		this(pageNo, pageRecordSize);

		this.totalRecord = totalRecord;

        this.calcTotalPage();
	}


    public void calcTotalPage() {
        this.totalPage = (this.totalRecord + this.pageRecordSize - 1) / this.pageRecordSize;
    }


    public long resetReasonablePageNo() {
        if ( this.pageNo > totalPage) {
            this.pageNo = totalPage;
        }
        this.pageNo = pageNo < 1 ? 1L : pageNo;
        return this.pageNo;
    }




	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(long pageNo) {
		this.pageNo = pageNo;
	}

	public long getPageRecordSize() {
		return pageRecordSize;
	}

	public void setPageRecordSize(long pageRecordSize) {
		this.pageRecordSize = pageRecordSize;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
