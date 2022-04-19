package h2o.common.util.format;

import h2o.common.util.format.stringformat.GroupProcessor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {

    private StringFormatter() {
    }

    public static String groupReplace(String str, String pattern, String v) {

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);

        return m.replaceAll(v);

    }


    public static String groupReplace(String str, String pattern, GroupProcessor gp) {

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);

        StringBuffer sb = new StringBuffer();

        int gsize = m.groupCount() + 1;
        String[] groups = new String[gsize];

        while (m.find()) {

            for (int i = 0; i < gsize; i++) {
                groups[i] = m.group(i);
            }

            m.appendReplacement(sb, gp.proc(groups));
        }
        m.appendTail(sb);

        return sb.toString();

    }

}
