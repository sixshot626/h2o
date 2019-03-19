package h2o.test.beanutil;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Aaa {

    private String aaaBBBccc;

    private String CCC;

    public String getAaaBBBccc() {
        return aaaBBBccc;
    }

    public void setAaaBBBccc(String aaaBBBccc) {
        this.aaaBBBccc = aaaBBBccc;
    }

    public String getCCC() {
        return CCC;
    }

    public void setCCC(String CCC) {
        this.CCC = CCC;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("aaaBBBccc", aaaBBBccc)
                .append("CCC", CCC)
                .toString();
    }
}
