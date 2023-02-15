package h2o.common.data.domain;

import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;

import java.util.Collections;
import java.util.List;

public class PageRequest implements java.io.Serializable {

    private static final long serialVersionUID = 1445323518186619685L;

    private long pageNo;

    private long pageSize;

    private List<SortInfo> sorts;

    public PageRequest() {
    }

    public PageRequest(long pageNo, long pageSize) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageRequest(long pageNo, long pageSize, SortInfo... sorts) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sorts = CollectionUtil.argsIsBlank(sorts) ? null :
                Collections.unmodifiableList(ListBuilder.newList(sorts));
    }

    public PageRequest(long pageNo, long pageSize, List<SortInfo> sorts) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.sorts = Collections.unmodifiableList(sorts);
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
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
        sb.append(", pageSize=").append(pageSize);
        sb.append(", sorts=").append(sorts);
        sb.append('}');
        return sb.toString();
    }
}
