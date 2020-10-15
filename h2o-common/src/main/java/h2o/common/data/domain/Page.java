package h2o.common.data.domain;

import java.util.List;

public class Page<T> implements java.io.Serializable {

	private static final long serialVersionUID = -4017120973784492101L;

	private Pageable pageInfo;

	private List<T> content;

	public Page() {}

	public Page( Pageable pageInfo , List<T> content) {
	    this.pageInfo = pageInfo;
	    this.content = content;
    }


	public Pageable getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(Pageable pageInfo) {
		this.pageInfo = pageInfo;
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
		sb.append("pageInfo=").append(pageInfo);
		sb.append(", content=").append(content);
		sb.append('}');
		return sb.toString();
	}
}
