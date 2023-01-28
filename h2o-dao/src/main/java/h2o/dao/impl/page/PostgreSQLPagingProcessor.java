package h2o.dao.impl.page;

import h2o.common.data.domain.ResultInfo;
import h2o.common.lang.tuple.Tuple;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.MapBuilder;
import h2o.common.util.lang.StringUtil;
import h2o.dao.page.PagingProcessor;

import java.util.Map;

public class PostgreSQLPagingProcessor extends AbstractPagingProcessor implements PagingProcessor {

    private static final String P1 = "page_row_start";
    private static final String P2 = "page_row_size";

    @Override
    public Tuple2<String, Map<String, Object>> pagingSql(String sql, ResultInfo resultInfo) {

        Long pageRowStart = resultInfo.getStart();
        Long pageRowSize = resultInfo.getSize();

        Map<String, Object> args = MapBuilder.so(2)
                .put(P1, pageRowStart)
                .put(P2, pageRowSize).get();

        StringBuilder pageSql = new StringBuilder();
        pageSql.append("select * from (\n\n");
        pageSql.append(this.orderProc(sql, resultInfo.getSorts()));
        StringUtil.append(pageSql, "\n\n) page_query limit :", P2, " offset :", P1);

        return Tuple.t(pageSql.toString(), args);
    }

}
