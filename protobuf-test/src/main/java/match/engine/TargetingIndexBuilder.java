package match.engine;

import org.apache.commons.codec.digest.DigestUtils;
import targeting.TargetingBe;
import targeting.TargetingIndexOuterClass;
import util.Tuble;

import java.util.*;

public class TargetingIndexBuilder {
    BetreeParser betreeParser = new BetreeParser();

    TargetingIndexOuterClass.TargetingIndex.Builder targetingIndexB;
    //key-->conj的MD5
    //value-->conjId
    private Map<String, Integer> conjFingerMap;
    private List<TargetingIndexOuterClass.ConjunctionIndex.Builder> conjIndexVec = new ArrayList<>();
    private List<TargetingIndexOuterClass.ConjunctionIndex.Builder> newConjIndexVec;


    private Map<TargetingIndexOuterClass.Interval, Integer> intervalMap;
    private List<TargetingIndexOuterClass.Interval> intervalVec = new ArrayList<>();


    private List<TargetingIndexOuterClass.TokenIndex.Builder> tokenIndexVec = new ArrayList<>();

    private List<TargetingIndexOuterClass.AidIndex> aidIndexVec = new ArrayList<>();

    private List<TargetingIndexOuterClass.AdIndex> adIndexVec = new ArrayList<>();


    private int[] conjIdRemapping;

    private TargetingIndexOuterClass.IntervalCoverIndex.Builder intervalIndexb;

    public TargetingIndexBuilder(TargetingIndexOuterClass.TargetingIndex.Builder index) {
        this.targetingIndexB = index;
        this.intervalMap = new HashMap<>();
        this.conjFingerMap = new HashMap<>();
    }

    public void build(List<TargetingBe.TargetingBE> beVec) {
        for (int i = 0; i < beVec.size(); i++) {
            TargetingBe.TargetingBE be = beVec.get(i);
            int localId = i;
            processBe(be, localId);
        }

        idRemapping();
        reArrangeData();
        intervalIndexb = TargetingIndexOuterClass.IntervalCoverIndex.newBuilder();
        BuildIntervalCoverIndex(intervalMap, intervalIndexb);
        saveData();
    }

    private void saveData() {
        this.targetingIndexB.addAllIntervalData(this.intervalVec);
        this.targetingIndexB.addAllAdIndex(this.adIndexVec);
        this.targetingIndexB.addAllAidIndex(this.aidIndexVec);
        List<TargetingIndexOuterClass.TokenIndex.Builder> tokenIndexVec = this.tokenIndexVec;
        for (TargetingIndexOuterClass.TokenIndex.Builder b : tokenIndexVec) {
            this.targetingIndexB.addTokenIndex(b);
        }
        List<TargetingIndexOuterClass.ConjunctionIndex.Builder> newConjIndexVec = this.newConjIndexVec;
        for (TargetingIndexOuterClass.ConjunctionIndex.Builder b : newConjIndexVec) {
            this.targetingIndexB.addConjunctionIndex(b);
        }
//        this.targetingIndexB.addAllTokenIndex(this.tokenIndexVec);
//        this.targetingIndexB.addAllConjunctionIndex(this.newConjIndexVec);
        this.targetingIndexB.setIntervalCoverIndex(this.intervalIndexb);
    }

    private void BuildIntervalCoverIndex(Map<TargetingIndexOuterClass.Interval, Integer> rangeMapping, TargetingIndexOuterClass.IntervalCoverIndex.Builder ri) {
        Map<Long, Boolean> existMap = new HashMap<>();
        existMap.put(0L, true);
        existMap.put(Long.MAX_VALUE, true);

        for (TargetingIndexOuterClass.Interval r : rangeMapping.keySet()) {
            existMap.put(r.getBegin(), true);
            existMap.put(r.getEnd(), true);
        }

        for (Long p : existMap.keySet()) {
            TargetingIndexOuterClass.IntervalCoverIndex.PointInfo.Builder ppB = TargetingIndexOuterClass.IntervalCoverIndex.PointInfo.newBuilder();
            ppB.setPoint(p);
            ri.addPoints(ppB);
        }
        //排序
        Collections.sort(ri.getPointsList(), new Comparator<targeting.TargetingIndexOuterClass.IntervalCoverIndex.PointInfo>() {

            @Override
            public int compare(TargetingIndexOuterClass.IntervalCoverIndex.PointInfo o1, TargetingIndexOuterClass.IntervalCoverIndex.PointInfo o2) {
                return (int) (o1.getPoint() - o2.getPoint());
            }
        });
        //ri.getPointsList();
        Map<Long, Integer> pMapIndex = new HashMap<>();
        List<TargetingIndexOuterClass.IntervalCoverIndex.PointInfo> pointsList = ri.getPointsList();
        for (int i = 0; i < ri.getPointsCount(); i++) {
            TargetingIndexOuterClass.IntervalCoverIndex.PointInfo p = pointsList.get(i);
            pMapIndex.put(p.getPoint(), i);
        }

        for (Map.Entry<TargetingIndexOuterClass.Interval, Integer> entry : rangeMapping.entrySet()) {
            Integer b = pMapIndex.get(entry.getKey().getBegin());
            Integer e = pMapIndex.get(entry.getKey().getEnd());
            for (int i = b.intValue(); i < e.intValue(); i++) {
                ri.getPointsBuilder(i).addIds(entry.getValue());
            }
        }

        for (TargetingIndexOuterClass.IntervalCoverIndex.PointInfo p : ri.getPointsList()) {
            Collections.sort(p.getIdsList());
        }

    }

    private void idRemapping() {
        int max_hit_count = 0;
        for (TargetingIndexOuterClass.ConjunctionIndex.Builder conjIndexB : conjIndexVec) {
            TargetingIndexOuterClass.ConjunctionIndex index = conjIndexB.build();
            if (index.getHitCount() > max_hit_count) {
                max_hit_count = index.getHitCount();
            }
        }
        List<List<Integer>> hitList = new ArrayList<>(max_hit_count + 1);
        for (int i = 0; i < max_hit_count + 1; i++) {
            hitList.add(new ArrayList<>());
        }
//        int[][] hitList = new int[max_hit_count + 1][max_hit_count + 1];
        for (int i = 0; i < conjIndexVec.size(); i++) {
            TargetingIndexOuterClass.ConjunctionIndex index = conjIndexVec.get(i).build();
            int hitCount = index.getHitCount();
            hitList.get(hitCount).add(i);
        }

        int newId = 0;
        conjIdRemapping = new int[conjIndexVec.size()];
        for (List<Integer> slice : hitList) {
            for (Integer oldId : slice) {
                conjIdRemapping[oldId.intValue()] = newId;
                newId++;
            }
        }
    }

    private void reArrangeData() {
        //
        newConjIndexVec = new ArrayList<>(conjIndexVec.size());
        for (int i = 0; i < conjIndexVec.size(); i++) {
            newConjIndexVec.add(null);
        }
        for (int i = 0; i < conjIndexVec.size(); i++) {
            int newId = conjIdRemapping[i];
            newConjIndexVec.set(newId, conjIndexVec.get(i));
        }
        //
        for (TargetingIndexOuterClass.TokenIndex.Builder tokenIndexB : tokenIndexVec) {
            for (TargetingIndexOuterClass.TokenIndex.ConjunctionHit.Builder conjHitB : tokenIndexB.getConjunctionHitBuilderList()) {
                int oldId = conjHitB.getConjunctionId();
                conjHitB.setConjunctionId(conjIdRemapping[oldId]);
            }
            List<TargetingIndexOuterClass.TokenIndex.ConjunctionHit> conjunctionHitList = tokenIndexB.getConjunctionHitList();
            Collections.sort(tokenIndexB.getConjunctionHitList(), new Comparator<TargetingIndexOuterClass.TokenIndex.ConjunctionHit>() {
                @Override
                public int compare(TargetingIndexOuterClass.TokenIndex.ConjunctionHit o1, TargetingIndexOuterClass.TokenIndex.ConjunctionHit o2) {
                    return o1.getConjunctionId()-o2.getConjunctionId();
                }
            });
            //排序
//            Collections.sort(tokenIndexB.getConjunctionHitBuilderList(), new Comparator<TargetingIndexOuterClass.TokenIndex.ConjunctionHit.Builder>() {
//                @Override
//                public int compare(TargetingIndexOuterClass.TokenIndex.ConjunctionHit.Builder o1, TargetingIndexOuterClass.TokenIndex.ConjunctionHit.Builder o2) {
//                    return o1.getConjunctionId() - o2.getConjunctionId();
//                }
//            });
        }
        //排序
        Collections.sort(aidIndexVec, new Comparator<TargetingIndexOuterClass.AidIndex>() {
            @Override
            public int compare(TargetingIndexOuterClass.AidIndex o1, TargetingIndexOuterClass.AidIndex o2) {
                return (int) (o1.getAid() - o2.getAid());
            }
        });

    }

    private void processBe(TargetingBe.TargetingBE be, int localId) {
        TargetingBe.TargetingDNF.Builder dnfBuilder = TargetingBe.TargetingDNF.newBuilder();
        betreeParser.convertToTargetingDNF(be, dnfBuilder);
        TargetingBe.TargetingDNF dnf = dnfBuilder.build();

        Map<Integer, Boolean> ad_token_set = new TreeMap<>();
        List<TargetingBe.TargetingDNF.Conjunction> conjunctionList = dnf.getConjunctionList();
        for (TargetingBe.TargetingDNF.Conjunction conj : conjunctionList) {
            String finger = calcConjFinger(conj);
            System.out.println(finger);
            TargetingIndexOuterClass.ConjunctionIndex.Builder conjIndexB = TargetingIndexOuterClass.ConjunctionIndex.newBuilder();
            Integer conjId;

            boolean conjExist = conjFingerMap.containsKey(finger);
            if (conjExist) {
                conjId = conjFingerMap.get(finger);
                conjIndexB = conjIndexVec.get(conjId);
            } else {
                conjId = conjIndexVec.size();
                conjFingerMap.put(finger, conjId);
                conjIndexB = TargetingIndexOuterClass.ConjunctionIndex.newBuilder();
                conjIndexVec.add(conjIndexB);
            }

            List<TargetingBe.Predicate> preVec = new ArrayList<>();
            List<TargetingBe.Predicate> notPreVec = new ArrayList<>();
            List<TargetingBe.Predicate> predicateList = conj.getPredicateList();
            for (TargetingBe.Predicate predicate : predicateList) {
                if (predicate.getNot()) {
                    notPreVec.add(predicate);
                } else {
                    preVec.add(predicate);
                }
            }
            //
            processConjunction(localId, conjId, conjExist, preVec, notPreVec, ad_token_set);
            //
            conjIndexB.setHitCount(preVec.size());
            conjIndexB.addLocalIds(localId);
        }
        //
        aidIndexVec.add(TargetingIndexOuterClass.AidIndex.newBuilder().setAid(be.getAid()).setLocalId(localId).build());
        TargetingIndexOuterClass.AdIndex.Builder adIndexB = TargetingIndexOuterClass.AdIndex.newBuilder();
        adIndexB.setAid(be.getAid());
        adIndexB.setAdinfoId(localId);
        for (Integer tokenId : ad_token_set.keySet()) {
            adIndexB.addTokenId(tokenId);
        }
        adIndexVec.add(adIndexB.build());
    }

    private void processConjunction(int localId, Integer conjId, boolean conjExist, List<TargetingBe.Predicate> preVec, List<TargetingBe.Predicate> notPreVec, Map<Integer, Boolean> ad_token_set) {

        for (int p = 0; p < preVec.size(); p++) {
            TargetingBe.Predicate predicate = preVec.get(p);
            int pid = p;
            for (TargetingBe.Predicate.Value value : predicate.getValueList()) {
                TargetingIndexOuterClass.Interval.Builder intervalB = TargetingIndexOuterClass.Interval.newBuilder();
                TargetingIndexOuterClass.TokenIndex.AdHit.Builder adHitB = TargetingIndexOuterClass.TokenIndex.AdHit.newBuilder();
                if (value.getType() == TargetingBe.Predicate.Value.Type.ID) {
                    intervalB
                            .setBegin(value.getId())
                            .setEnd(value.getId() + 1).build();

                    adHitB
                            .setLocalId(localId)
                            .setIsId(true).build();
                } else if (value.getType() == TargetingBe.Predicate.Value.Type.RANGE) {
                    intervalB
                            .setBegin(value.getRange().getBegin())
                            .setEnd(value.getRange().getEnd()).build();

                    adHitB
                            .setLocalId(localId)
                            .setIsId(false).build();
                }

                Tuble<Integer, TargetingIndexOuterClass.TokenIndex.Builder> tuble = getToken(intervalB.build());
                if (!conjExist) {

                    TargetingIndexOuterClass.TokenIndex.ConjunctionHit conjunctionHit = TargetingIndexOuterClass.TokenIndex.ConjunctionHit.newBuilder()
                            .setConjunctionId(conjId)
                            .setPredicateId(pid).build();

                    tuble._2().addConjunctionHit(conjunctionHit);
                }

                boolean ok = ad_token_set.containsKey(tuble._1());
                if (!ok) {
                    ad_token_set.put(tuble._1(), true);
                    tuble._2().addAdHit(adHitB);
                }
            }
        }

        if (conjExist) {
            return;
        }

        for (int p = 0; p < notPreVec.size(); p++) {
            int pid = p + preVec.size();
            TargetingBe.Predicate predicate = notPreVec.get(p);
            for (TargetingBe.Predicate.Value value : predicate.getValueList()) {
                TargetingIndexOuterClass.Interval.Builder intervalB = TargetingIndexOuterClass.Interval.newBuilder();
                if (value.getType() == TargetingBe.Predicate.Value.Type.ID) {
                    intervalB
                            .setBegin(value.getId())
                            .setEnd(value.getId() + 1).build();
                } else if (value.getType() == TargetingBe.Predicate.Value.Type.RANGE) {
                    intervalB
                            .setBegin(value.getRange().getBegin())
                            .setEnd(value.getRange().getEnd()).build();
                }

                Tuble<Integer, TargetingIndexOuterClass.TokenIndex.Builder> tuble = getToken(intervalB.build());
                tuble._2().addConjunctionHit(
                        TargetingIndexOuterClass
                                .TokenIndex
                                .ConjunctionHit
                                .newBuilder()
                                .setConjunctionId(conjId)
                                .setPredicateId(pid).build()
                );
            }
        }
    }

    public Tuble<Integer, TargetingIndexOuterClass.TokenIndex.Builder> getToken(TargetingIndexOuterClass.Interval interval) {
        TargetingIndexOuterClass.TokenIndex.Builder tokenIndexB;
        Integer tokenId;
        boolean ok = intervalMap.containsKey(interval);
        if (!ok) {
            tokenId = intervalVec.size();
            intervalVec.add(interval);
            intervalMap.put(interval, tokenId);
            tokenIndexB = TargetingIndexOuterClass.TokenIndex.newBuilder();

            tokenIndexVec.add(tokenIndexB);
        } else {
            tokenId = intervalMap.get(interval);
            tokenIndexB = tokenIndexVec.get(tokenId);
        }
        return new Tuble<Integer, TargetingIndexOuterClass.TokenIndex.Builder>(tokenId, tokenIndexB);
    }


    private String calcConjFinger(TargetingBe.TargetingDNF.Conjunction conj) {
        byte[] data = conj.toByteArray();
        return DigestUtils.md5Hex(data);
    }
}
