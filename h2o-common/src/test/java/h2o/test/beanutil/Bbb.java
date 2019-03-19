package h2o.test.beanutil;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Bbb {

    private String aaabbbccc;

    private String ccC;


    public String getAaabbbccc() {
        return aaabbbccc;
    }

    public void setAaabbbccc(String aaabbbccc) {
        this.aaabbbccc = aaabbbccc;
    }

    public String getCcC() {
        return ccC;
    }

    public void setCcC(String ccC) {
        this.ccC = ccC;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("aaabbbccc", aaabbbccc)
                .append("ccC", ccC)
                .toString();
    }
}
