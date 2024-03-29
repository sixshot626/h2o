package h2o.dao.impl.page;

import h2o.apache.commons.lang.StringUtils;
import h2o.common.data.domain.SortInfo;
import h2o.common.lang.tuple.Tuple;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.CollectionUtil;
import h2o.dao.page.PagingProcessor;

import java.util.List;

public abstract class AbstractPagingProcessor implements PagingProcessor {


    @Override
    public Tuple2<String, String> totalSql(String sql) {

        String countSql = "select count(*) as count_num from (\n" + sql + "\n) page_count ";

        return Tuple.t(countSql, "count_num");
    }


    protected String orderProc(String sql, List<SortInfo> strts) {

        if (CollectionUtil.isEmpty(strts)) {
            return sql;
        }

        StringBuilder orderBy = new StringBuilder();
        int i = 0;
        for (SortInfo sortInfo : strts) {
            if (i++ > 0) {
                orderBy.append(",");
            }
            orderBy.append(sortInfo.toSqlString());
        }

        String schSql = StringUtils.contains(sql, ')') ?
                StringUtils.substringAfterLast(sql, ")") : sql;

        boolean hasOrderBy = StringUtils.contains(schSql, "order");

        if (hasOrderBy) {
            return sql + " , " + orderBy.toString();
        } else {
            return sql + " order by " + orderBy.toString();
        }

    }

}
