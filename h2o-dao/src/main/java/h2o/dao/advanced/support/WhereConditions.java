package h2o.dao.advanced.support;

import java.util.Map;

public interface WhereConditions {

    String whereSql();

    Map<Object,Object> params();

}
