package h2o.common.data.domain;

import h2o.common.collections.CollectionUtil;
import h2o.common.collections.builder.ListBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;

public class PageRequest implements java.io.Serializable {

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
        return ToStringBuilder.reflectionToString(this);
    }
}
