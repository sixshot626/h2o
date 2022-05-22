package h2o.dao;

import h2o.common.thirdparty.freemarker.TemplateUtil;
import h2o.dao.proc.ArgProcessor;
import h2o.dao.proc.OrmProcessor;
import h2o.dao.page.PagingProcessor;
import h2o.dao.proc.RowProcessor;
import h2o.dao.sql.SqlBuilder;
import h2o.dao.sql.SqlTable;

import java.util.Optional;

public interface DBFactory {

    SqlTable getSqlTable();

    TemplateUtil getSqlTemplateUtil();

    SqlBuilder getSqlBuilder();

    ArgProcessor getArgProcessor();

    RowProcessor getRowProcessor();

    OrmProcessor getOrmProcessor();

    Optional<PagingProcessor> getPagingProcessor(String name);

    // 获取name对应单例Db对象
    Db getDb(String name);

    // 创建新Db对象
    Db createDb(String name);

    DbUtil getDbUtil();

}
