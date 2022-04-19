package h2o.common.data.domain;

import java.util.List;

public class ResultInfo implements java.io.Serializable {

    private static final long serialVersionUID = 1607703985782895368L;

    private long start;
    private long size;

    private List<SortInfo> sorts;

    public ResultInfo() {
    }

    public ResultInfo(int size) {
        this.setSize(size);
        this.setStart(0);
    }

    public ResultInfo(long start, long size) {
        this.setSize(size);
        this.setStart(start);
    }

    public ResultInfo(Pageable pageInfo) {
        this.setSize(pageInfo.getPageSize());
        this.setStart((pageInfo.getPageNo() - 1) * pageInfo.getPageSize());
    }

    public ResultInfo(PageRequest pageRequest) {
        this.setSize(pageRequest.getPageSize());
        this.setStart((pageRequest.getPageNo() - 1) * pageRequest.getPageSize());
        this.sorts = pageRequest.getSorts();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public List<SortInfo> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortInfo> sorts) {
        this.sorts = sorts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultInfo{");
        sb.append("start=").append(start);
        sb.append(", size=").append(size);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
