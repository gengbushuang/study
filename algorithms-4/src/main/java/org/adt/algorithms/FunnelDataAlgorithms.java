package org.adt.algorithms;

import org.adt.algorithms.data.FunnelData;
import org.adt.algorithms.util.DataUtils;
import org.adt.algorithms.util.Pair;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.util.*;

/**
 * @author gengbushuang
 * @date 2024/5/14 10:32
 * 有序漏斗
 */
public class FunnelDataAlgorithms {

    private String dataPattern = "yyyy-MM-dd HH:mm:ss";

    private Map<Integer, List<Pair<Long, Integer>>> map = new HashMap<>();

    private Comparator<Pair<Long, Integer>> pairComparator = (o1, o2) -> {
        if (o1.getFirst() - o2.getFirst() == 0) {
            return o1.getSecond() - o2.getSecond();
        }
        return (int) (o1.getFirst() - o2.getFirst());
    };

    public void windowFunnel(long window, List<String> events) {
        List<FunnelData> funnelDataList = DataUtils.initFunnelDataList();
        for (FunnelData funnelData : funnelDataList) {
            int index = getIndex(events, funnelData.getEvent());
            if (index < 0) {
                continue;
            }
            long time;
            try {
                time = DateUtils.parseDate(funnelData.getDay(), dataPattern).getTime();
            } catch (ParseException e) {
                continue;
            }

            int userId = funnelData.getUserId();
            List<Pair<Long, Integer>> pairs = map.computeIfAbsent(userId, (key) -> new ArrayList<>());
            pairs.add(Pair.create(time, index));
        }

        System.out.println(map);

        Set<Map.Entry<Integer, List<Pair<Long, Integer>>>> entries = map.entrySet();
        for (Map.Entry<Integer, List<Pair<Long, Integer>>> setPair : entries) {
            int level = _windowFunnel(window * 1000, events.size(), setPair.getValue());
            System.out.println(setPair.getKey()+"___"+level);
        }



    }

    private int _windowFunnel(long window, int eventSize, List<Pair<Long, Integer>> pairs) {
        pairs.sort(pairComparator);
        long[] eventsTimestamp = new long[eventSize];
        Arrays.fill(eventsTimestamp, -1);
        for (Pair<Long, Integer> pair : pairs) {
            Long timestamp = pair.getFirst();
            Integer event_index = pair.getSecond();
            if (event_index.intValue() == 0) {
                eventsTimestamp[0] = timestamp;
            } else if (eventsTimestamp[event_index.intValue() - 1] > 0 && (timestamp.longValue() - eventsTimestamp[event_index.intValue() - 1]) <= window) {
                eventsTimestamp[event_index.intValue()] = timestamp.longValue();
                if (event_index.intValue() + 1 == eventSize) {
                    return eventSize;
                }
            }
        }

        for (int i = eventsTimestamp.length; i > 0; i--) {
            if (eventsTimestamp[i - 1] > 0) {
                return i;
            }
        }

        return 0;
    }


    private int getIndex(List<String> events, String event) {
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).equals(event)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws ParseException {
        FunnelDataAlgorithms funnelDataAlgorithms = new FunnelDataAlgorithms();
        List<String> events = new ArrayList<>();
//        events.add("启动");
//        events.add("首页");
        events.add("详情");
        events.add("下载");

        funnelDataAlgorithms.windowFunnel(86400, events);
    }

}
