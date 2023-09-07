package h2o.common.util.valid;

import h2o.common.lang.MVal;
import h2o.common.lang.tuple.Tuple;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.ListBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ValidateUtil {

    private ValidateUtil() {
    }

    public static List<MVal<String>> validate(Object bean, Validator... validators) {

        List<MVal<String>> rs = ListBuilder.newList();
        Set<String> ks = new HashSet<>();
        for (Validator validator : validators) {

            String k = validator.getK();
            if (ks.contains(k)) {
                continue;
            }
            if (!validator.validate(bean)) {
                rs.add(new MVal<>( k , validator.getMessage() ));
                ks.add(k);
            }
        }

        return rs;
    }

    public static MVal<String> validateOne(Object bean, Validator... validators) {

        for (Validator validator : validators) {
            if (!validator.validate(bean)) {
                return new MVal(validator.getK(),validator.getMessage());
            }
        }
        return MVal.empty();
    }

}
