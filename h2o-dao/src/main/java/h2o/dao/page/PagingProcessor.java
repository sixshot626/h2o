package h2o.dao.page;

import h2o.common.collections.tuple.Tuple2;
import h2o.common.data.domain.PageRequest;

import java.util.Map;

public interface PagingProcessor {

    Tuple2<String, String> totalSql(String sql);

    Tuple2<String, Map<String,Object>> pagingSql(String sql, PageRequest pageRequest);

}
