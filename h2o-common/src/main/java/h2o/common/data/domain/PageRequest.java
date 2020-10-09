package h2o.common.data.domain;

import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;

import java.util.List;

public class PageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1445323518186619685L;

    private long pageNo;

    private long pageRecordSize;

    private List<SortInfo> sorts;


    public PageRequest(long pageNo, long pageRecordSize) {
        this.pageNo = pageNo;
        this.pageRecordSize = pageRecordSize;
    }

    public PageRequest(long pageNo, long pageRecordSize, SortInfo... sorts) {
        this.pageNo = pageNo;
        this.pageRecordSize = pageRecordSize;
        this.sorts = CollectionUtil.argsIsBlank(sorts) ? null : ListBuilder.newList(sorts);
    }

    public PageRequest(long pageNo, long pageRecordSize, List<SortInfo> sorts) {
        this.pageNo = pageNo;
        this.pageRecordSize = pageRecordSize;
        this.sorts = sorts;
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

    public List<SortInfo> getSorts() {
        return sorts;
    }

    public void setSorts(List<SortInfo> sorts) {
        this.sorts = sorts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PageRequest{");
        sb.append("pageNo=").append(pageNo);
        sb.append(", pageRecordSize=").append(pageRecordSize);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
