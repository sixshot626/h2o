package h2o.common.util.data;

import h2o.common.data.domain.PageInfo;
import h2o.common.data.domain.ResultInfo;
import h2o.common.lang.tuple.Tuple;
import h2o.common.lang.tuple.Tuple2;
import h2o.common.util.collection.CollectionUtil;
import h2o.common.util.collection.ListBuilder;

import java.util.List;

public class MultiSourcePage {

    private final List<Integer> soureceInfo = ListBuilder.newList();

    private int totalRecord;

    public MultiSourcePage(Integer... sizes) {
        if (!CollectionUtil.argsIsBlank(sizes)) {
            for (Integer s : sizes) {
                soureceInfo.add(s);
                totalRecord += s;
            }
        }
    }


    public Tuple2<PageInfo, ResultInfo[]> calc(int start, int pageRecordSize) {

        PageInfo pageInfo = new PageInfo((start + pageRecordSize - 1) / pageRecordSize, pageRecordSize, totalRecord);


        int first = start;
        int end = first + pageRecordSize - 1;
        if (end > (totalRecord - 1)) end = totalRecord - 1;

        ResultInfo[] resultInfos = new ResultInfo[soureceInfo.size()];

        int index = 0;

        for (int i = 0; i < resultInfos.length; i++) {

            int t = soureceInfo.get(i);
            if (t <= 0) {
                continue;
            }
            int f = 0;
            int m = 0;

            int e = index + t - 1;
            if (e >= first) {

                f = index >= first ? 0 : first - index;

                m = t - f;
                if (e > end) {
                    m -= (e - end);
                }

                resultInfos[i] = new ResultInfo(f, m);

                if (e >= end) {
                    break;
                }
            }

            index = e + 1;


        }


        return Tuple.t(pageInfo, resultInfos);

    }


}
