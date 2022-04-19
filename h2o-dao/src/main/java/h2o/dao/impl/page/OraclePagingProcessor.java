package h2o.dao.impl.page;

import h2o.common.data.domain.PageRequest;
import h2o.common.data.domain.ResultInfo;
import h2o.common.lang.tuple.Tuple;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.MapBuilder;
import h2o.common.util.lang.StringUtil;
import h2o.dao.page.PagingProcessor;

import java.util.Map;

public class OraclePagingProcessor extends AbstractPagingProcessor implements PagingProcessor {

    private static final String P1 = "page_row_num1";
    private static final String P2 = "page_row_num2";

    @Override
    public Tuple2<String, Map<String, Object>> pagingSql(String sql, PageRequest pageRequest) {

        ResultInfo resultInfo = new ResultInfo(pageRequest);

        Long pageRowNum1 = resultInfo.getStart() + 1;
        Long pageRowNum2 = resultInfo.getStart() + resultInfo.getSize();

        Map<String, Object> args = MapBuilder.so(2)
                .put(P1, pageRowNum1)
                .put(P2, pageRowNum2).get();

        StringBuilder pageSql = new StringBuilder();
        pageSql.append("select * from (\n    select page_query.*, rownum as page_row_num from (\n");
        pageSql.append(this.orderProc(sql, pageRequest.getSorts()));
        StringUtil.append(pageSql, "\n    ) page_query  where rownum <= :", P2, " \n) where page_row_num >= :", P1);

        return Tuple.t(pageSql.toString(), args);
    }

}
