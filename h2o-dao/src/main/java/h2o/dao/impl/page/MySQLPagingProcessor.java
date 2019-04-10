package h2o.dao.impl.page;

import h2o.common.bean.page.PageRequest;
import h2o.common.bean.page.ResultInfo;
import h2o.common.collections.builder.MapBuilder;
import h2o.common.collections.tuple.Tuple2;
import h2o.common.collections.tuple.TupleUtil;
import h2o.common.util.lang.StringUtil;
import h2o.dao.page.PagingProcessor;

import java.util.Map;

public class MySQLPagingProcessor extends AbstractPagingProcessor implements PagingProcessor {

    private static final String P1 = "page_row_start";
    private static final String P2 = "page_row_size";

    @Override
    public Tuple2<String, Map<String, Object>> pagingSql(String sql, PageRequest pageRequest) {

        ResultInfo resultInfo = new ResultInfo( pageRequest );

        Long pageRowStart = resultInfo.getFirstResult();
        Long pageRowSize = resultInfo.getMaxResult();

        Map<String,Object> args = MapBuilder.so(2)
                .put( P1 , pageRowStart )
                .put( P2 , pageRowSize ).get();

        StringBuilder pageSql = new StringBuilder();
        pageSql.append("select * from (\n");
        pageSql.append( this.orderProc( sql , pageRequest.getSorts() ) );
        StringUtil.append(pageSql , "\n) page_query limit :" , P1 , ", :" , P2);

        return TupleUtil.t( pageSql.toString() , args);
    }

}