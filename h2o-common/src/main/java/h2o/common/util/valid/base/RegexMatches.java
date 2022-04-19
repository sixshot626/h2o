package h2o.common.util.valid.base;

import h2o.common.util.valid.Validator;

public class RegexMatches extends AbstractValidator implements Validator {

    private String regex;

    private boolean nullAble = false;

    public RegexMatches(String regex) {
        this.regex = regex;
    }

    public RegexMatches(String regex, boolean nullAble) {
        this.regex = regex;
        this.nullAble = nullAble;
    }

    @Override
    protected boolean validateV(Object v) {
        if (v == null) {
            return nullAble;
        }
        String s = (String) v;
        return s.matches(regex);
    }


}
