package h2o.dao.page;

import h2o.common.data.domain.ResultInfo;
import h2o.common.lang.tuple.Tuple2;

import java.util.Map;

public interface PagingProcessor {

    Tuple2<String, String> totalSql(String sql);

    Tuple2<String, Map<String, Object>> pagingSql(String sql, ResultInfo resultInfo);

}
