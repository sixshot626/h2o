package h2o.common.util.security.support;

import h2o.common.util.security.Seasoner;

public class SimpleSeasoner implements Seasoner {

    public String season(String str, String salt) {
        return str + "_" + salt;
    }

}
