package com.retrieval.news;

import com.proto.indexer.Indexer;
import com.utils.Pair;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;

public class NewIndexProcessor {

    private Map<String, Integer> conjFingerMap = new HashMap<>();
    private Map<Range, Integer> rangeMap = new HashMap<>();

    private List<Range> rangeVec = new ArrayList<>();

    private List<ConjunctionIndex> conjIndexVec = new ArrayList<>();
    private List<TokenIndex> tokenIndexVec = new ArrayList<>();
    private List<AidIndex> aidIndexVec = new ArrayList<>();
    private List<AdIndex> adIndexVec = new ArrayList<>();

    private int[] conjIdRemapping;

    private ConjunctionIndex[] newConjIndexVec;

    private IntervalCoverIndex intervalCoverIndex;

    void build(Indexer.TargetingDNF dnf, int localId) {
        long uniqueId = dnf.getUniqueId();
        Map<Integer, Boolean> ad_token_set = new HashMap<>();

        List<Indexer.Conjunction> conjunctionsList = dnf.getConjunctionsList();
        for (Indexer.Conjunction conj : conjunctionsList) {
            String finger = calcConjFinger(conj);
            boolean conjExist = conjFingerMap.containsKey(finger);
            ConjunctionIndex conjIndex;
            int conjId;
            if (conjExist) {
                conjId = conjFingerMap.get(finger).intValue();
                conjIndex = conjIndexVec.get(conjId);
            } else {
                conjId = conjIndexVec.size();
                conjIndex = new ConjunctionIndex();
                conjFingerMap.put(finger, Integer.valueOf(conjId));
                conjIndexVec.add(conjIndex);
            }
            List<Indexer.Assignment> assignmentsList = conj.getAssignmentsList();
            List<Indexer.Assignment> notAssignVec = new ArrayList<>();
            List<Indexer.Assignment> assignVec = new ArrayList<>();
            for (Indexer.Assignment assig : assignmentsList) {
                if (assig.getNot()) {
                    notAssignVec.add(assig);
                } else {
                    assignVec.add(assig);
                }
            }

            ProcessConjunction(localId, conjId, conjExist, assignVec, notAssignVec, ad_token_set);


            conjIndex.setHitCount(assignVec.size());
            conjIndex.add(Integer.valueOf(localId));

        }
        aidIndexVec.add(new AidIndex(dnf.getUniqueId(), localId));

        final AdIndex adIndex = new AdIndex();
        adIndex.setAid(dnf.getUniqueId());
        adIndex.setAdInfoId(localId);

        for (Integer tokenId : ad_token_set.keySet()) {
            adIndex.addTokenId(tokenId);
        }
        adIndexVec.add(adIndex);


        //////////////////////////////////////////////
        int max = 0;
        for (ConjunctionIndex conjunctionIndex : conjIndexVec) {
            if (conjunctionIndex.getHitCount() > max) {
                max = conjunctionIndex.getHitCount();
            }
        }
        ArrayList<Integer>[] hitList = new ArrayList[max + 1];
        for (int i = 0; i < conjIndexVec.size(); i++) {
            ConjunctionIndex conjunctionIndex = conjIndexVec.get(i);
            int hitCount = conjunctionIndex.getHitCount();
            if (hitList[hitCount] == null) {
                hitList[hitCount] = new ArrayList<>();
            }
            hitList[hitCount].add(Integer.valueOf(i));
        }

        int newId = 0;
        this.conjIdRemapping = new int[conjIndexVec.size()];
        for (ArrayList<Integer> slice : hitList) {
            if (slice == null) {
                continue;
            }
            for (Integer oldId : slice) {
                conjIdRemapping[oldId.intValue()] = newId;
                newId++;
            }
        }

        //////////////////
        newConjIndexVec = new ConjunctionIndex[conjIndexVec.size()];
//        newConjIndexVec = new ArrayList<>(conjIndexVec.size());
        for (int i = 0; i < conjIndexVec.size(); i++) {
            int newtId = conjIdRemapping[i];
            newConjIndexVec[newtId] = conjIndexVec.get(i);
//            newConjIndexVec.set(newtId, conjIndexVec.get(i));
        }

        for (TokenIndex tokenIndex : tokenIndexVec) {
            for (ConjunctionHit conjHit : tokenIndex.getConjunctionHits()) {
                int oldId = conjHit.getConjId();
                conjHit.setConjId(conjIdRemapping[oldId]);
            }
            tokenIndex.getConjunctionHits().sort(new Comparator<ConjunctionHit>() {
                @Override
                public int compare(ConjunctionHit o1, ConjunctionHit o2) {
                    return o1.getConjId() - o2.getConjId();
                }
            });
        }

        aidIndexVec.sort(new Comparator<AidIndex>() {
            @Override
            public int compare(AidIndex o1, AidIndex o2) {
                return (int) (o1.getAid() - o2.getAid());
            }
        });
        /////////////////////////////////
        intervalCoverIndex = new IntervalCoverIndex();
        BuildIntervalCoverIndex(rangeMap,intervalCoverIndex);

    }

    private void BuildIntervalCoverIndex(Map<Range, Integer> rangeMap,IntervalCoverIndex intervalIndex) {
        Map<Long, Boolean> existMap = new HashMap<>();
//        existMap.put(0L, true);
//        existMap.put(Long.MAX_VALUE-1, true);

        for (Range range : rangeMap.keySet()) {
            existMap.put(range.getLeft(), true);
            existMap.put(range.getRight(), true);
        }

        for (Long p : existMap.keySet()) {
            PointInfo pointInfo = new PointInfo(p.longValue());
            intervalIndex.add(pointInfo);
        }

        intervalIndex.getPointInfos().sort(new Comparator<PointInfo>() {
            @Override
            public int compare(PointInfo o1, PointInfo o2) {
                return (int) (o1.getPoint() - o2.getPoint());
            }
        });

        Map<Long, Integer> pMapIndex = new HashMap<>();
        List<PointInfo> pointInfos = intervalIndex.getPointInfos();
        for (int i = 0; i < pointInfos.size(); i++) {
            PointInfo info = pointInfos.get(i);
            pMapIndex.put(Long.valueOf(info.getPoint()),Integer.valueOf(i));
        }

        for(Map.Entry<Range,Integer> entry:rangeMap.entrySet()){
            Long bR = Long.valueOf(entry.getKey().getLeft());
            Long eR = Long.valueOf(entry.getKey().getRight());
            Integer b = pMapIndex.get(bR);
            Integer e = pMapIndex.get(eR);
            for(int i = b.intValue();i<e.intValue();i++){
                intervalIndex.getPointInfos().get(Integer.valueOf(i)).add(entry.getValue());
            }
        }

        pointInfos = intervalIndex.getPointInfos();
        for (int i = 0; i < pointInfos.size(); i++) {
            intervalIndex.getPointInfos().get(Integer.valueOf(i)).sort();
        }
    }

    private void ProcessConjunction(int localId, int conjId, boolean conjExist, List<Indexer.Assignment> assignVec, List<Indexer.Assignment> notAssignVec, Map<Integer, Boolean> ad_token_set) {
        for (int i = 0; i < assignVec.size(); i++) {
            int p = i;
            Indexer.Assignment assign = assignVec.get(i);
            Indexer.AssignValue value = assign.getValue();
            if (value.getIvalCount() > 0) {
                List<Integer> ivalList = value.getIvalList();
                for (Integer val : ivalList) {
                    Range range = new Range(val, val + 1);
                    Pair<Integer, TokenIndex> indexPair = this.GetToken(range);
                    AdHit adHit = new AdHit(localId, true);

                    if (!conjExist) {
                        ConjunctionHit conjunctionHit = new ConjunctionHit(conjId, p);
                        indexPair.getRight().add(conjunctionHit);
                    }

                    if (!ad_token_set.containsKey(indexPair.getLeft())) {
                        ad_token_set.put(indexPair.getLeft(), Boolean.TRUE);
                        indexPair.getRight().add(adHit);
                    }
                }

            } else if (value.getIntervalsCount() > 0) {
                List<Indexer.Interval> intervalsList = value.getIntervalsList();
                for (Indexer.Interval interval : intervalsList) {
                    Range range = new Range(interval.getBegin(), interval.getEnd());
                    Pair<Integer, TokenIndex> indexPair = this.GetToken(range);
                    AdHit adHit = new AdHit(localId, false);

                    if (!conjExist) {
                        ConjunctionHit conjunctionHit = new ConjunctionHit(conjId, p);
                        indexPair.getRight().add(conjunctionHit);
                    }

                    if (!ad_token_set.containsKey(indexPair.getLeft())) {
                        ad_token_set.put(indexPair.getLeft(), Boolean.TRUE);
                        indexPair.getRight().add(adHit);
                    }
                }
            }
        }

        if (conjExist) {
            return;
        }

        for (int i = 0; i < notAssignVec.size(); i++) {
            int p = i + assignVec.size();
            Indexer.Assignment notAssign = notAssignVec.get(i);
            Indexer.AssignValue value = notAssign.getValue();
            if (value.getIvalCount() > 0) {
                List<Integer> ivalList = value.getIvalList();
                for (Integer val : ivalList) {
                    Range range = new Range(val, val + 1);
                    Pair<Integer, TokenIndex> indexPair = this.GetToken(range);
                    ConjunctionHit conjunctionHit = new ConjunctionHit(conjId, p);
                    indexPair.getRight().add(conjunctionHit);
                }

            } else if (value.getIntervalsCount() > 0) {
                List<Indexer.Interval> intervalsList = value.getIntervalsList();
                for (Indexer.Interval interval : intervalsList) {
                    Range range = new Range(interval.getBegin(), interval.getEnd());
                    Pair<Integer, TokenIndex> indexPair = this.GetToken(range);
                    ConjunctionHit conjunctionHit = new ConjunctionHit(conjId, p);
                    indexPair.getRight().add(conjunctionHit);
                }
            }
        }

    }

    public Pair<Integer, TokenIndex> GetToken(Range range) {
        TokenIndex tokenIndex;
        Integer tokenId;
        boolean containsKey = rangeMap.containsKey(range);
        if (containsKey) {
            tokenId = rangeMap.get(range);
            tokenIndex = tokenIndexVec.get(tokenId);
        } else {
            tokenId = Integer.valueOf(rangeVec.size());
            rangeVec.add(range);
            rangeMap.put(range, tokenId);
            tokenIndex = new TokenIndex();
            tokenIndexVec.add(tokenIndex);
        }
        return Pair.create(tokenId, tokenIndex);
    }

    public String calcConjFinger(Indexer.Conjunction conj) {
        byte[] data = conj.toByteArray();
        return DigestUtils.md5Hex(data);
    }
}
