package match.engine;

import org.apache.commons.codec.digest.DigestUtils;
import targeting.TargetingBe;
import targeting.TargetingIndexOuterClass;
import util.Tuble;

import java.util.*;

public class TargetingIndexBuilder {
    BetreeParser betreeParser = new BetreeParser();

    //key-->conj的MD5
    //value-->conjId
    private Map<String, Integer> conjFingerMap = new HashMap<>();
    private List<TargetingIndexOuterClass.ConjunctionIndex> conjIndexVec = new ArrayList<>();

    private Map<TargetingIndexOuterClass.Interval,Integer> intervalMap = new HashMap<>();
    private List<TargetingIndexOuterClass.Interval> intervalVec = new ArrayList<>();


    private List<TargetingIndexOuterClass.TokenIndex> tokenIndexVec = new ArrayList<>();


    public void build(List<TargetingBe.TargetingBE> beVec) {
        for (int i = 0; i < beVec.size(); i++) {
            TargetingBe.TargetingBE be = beVec.get(i);
            int localId = i;
            processBe(be, localId);
        }
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
            TargetingIndexOuterClass.ConjunctionIndex conjIndex;
            Integer conjId;
            boolean conjExist = conjFingerMap.containsKey(finger);
            if (conjExist) {
                conjId = conjFingerMap.get(finger);
                conjIndex = conjIndexVec.get(conjId);
            } else {
                conjId = conjIndexVec.size();
                conjFingerMap.put(finger, conjId);
                conjIndex = TargetingIndexOuterClass.ConjunctionIndex.newBuilder().build();
                conjIndexVec.add(conjIndex);
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

            processConjunction(localId, conjId, conjExist, preVec, notPreVec, ad_token_set);
        }
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
                            .setEnd(value.getId()+1).build();

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

                Tuble<Integer, TargetingIndexOuterClass.TokenIndex> tuble = getToken(intervalB.build());
                if(!conjExist){
                    TargetingIndexOuterClass.TokenIndex.ConjunctionHit conjunctionHit = TargetingIndexOuterClass.TokenIndex.ConjunctionHit.newBuilder()
                            .setConjunctionId(conjId)
                            .setPredicateId(pid).build();

                    TargetingIndexOuterClass.TokenIndex.newBuilder(tuble._2())
                            .addConjunctionHit(conjunctionHit);

                }

                boolean ok = ad_token_set.containsKey(tuble._1());
                if(!ok){
                    ad_token_set.put(tuble._1(),true);
                    TargetingIndexOuterClass.TokenIndex.newBuilder().addAdHit(adHitB);
                }
            }
        }
    }

    public Tuble<Integer,TargetingIndexOuterClass.TokenIndex> getToken(TargetingIndexOuterClass.Interval interval){
        TargetingIndexOuterClass.TokenIndex tokenIndex;
        Integer tokenId;
        boolean ok = intervalMap.containsKey(interval);
        if(!ok){
            tokenId = intervalVec.size();
            intervalVec.add(interval);
            intervalMap.put(interval,tokenId);
            tokenIndex = TargetingIndexOuterClass.TokenIndex.newBuilder().build();

            tokenIndexVec.add(tokenIndex);
        }else{
            tokenId = intervalMap.get(interval);
            tokenIndex = tokenIndexVec.get(tokenId);
        }
        return new Tuble<Integer,TargetingIndexOuterClass.TokenIndex>(tokenId,tokenIndex);
    }


    private String calcConjFinger(TargetingBe.TargetingDNF.Conjunction conj) {
        byte[] data = conj.toByteArray();
        return DigestUtils.md5Hex(data);
    }
}
