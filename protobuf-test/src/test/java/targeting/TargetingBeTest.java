package targeting;

import com.google.protobuf.TextFormat;
import match.engine.BetreeParser;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static targeting.TargetingBe.LogicalOp.And;
import static targeting.TargetingBe.LogicalOp.Or;
import static targeting.TargetingBe.Predicate.Value.Type.RANGE;

public class TargetingBeTest {
    //key-->conj的MD5
    //value-->conjId
    private Map<String, Integer> conjFingerMap = new HashMap<>();

    private List<TargetingIndexOuterClass.ConjunctionIndex> conjIndexVec = new ArrayList<>();

    @Test
    public void testBeTree() {
        TargetingBe.TargetingBE.Builder builder = TargetingBe.TargetingBE.newBuilder()
                .setAid(100)
                .setBetree(

                        TargetingBe.BETree.newBuilder()
                                .setOp(And)
                                .setNot(false)
                                .setPredicate(
                                        TargetingBe.Predicate.newBuilder()
                                                .addValue(
                                                        TargetingBe.Predicate.Value.newBuilder()
                                                                .setType(RANGE)
                                                                .setRange(
                                                                        TargetingBe.Predicate.Value.Range.newBuilder()
                                                                                .setBegin(1)
                                                                                .setEnd(2)
                                                                )
                                                )
                                )
                                .addBetree(
                                        TargetingBe.BETree.newBuilder()
                                                .setOp(Or)
                                                .setNot(false)
                                                .setPredicate(
                                                        TargetingBe.Predicate.newBuilder()
                                                                .addValue(
                                                                        TargetingBe.Predicate.Value.newBuilder()
                                                                                .setType(RANGE)
                                                                                .setRange(
                                                                                        TargetingBe.Predicate.Value.Range.newBuilder()
                                                                                                .setBegin(100)
                                                                                                .setEnd(200)
                                                                                )
                                                                )
                                                )
                                                .addBetree(
                                                        TargetingBe.BETree.newBuilder()
                                                                .setOp(And)
                                                                .setNot(false)
                                                                .setPredicate(
                                                                        TargetingBe.Predicate.newBuilder()
                                                                                .addValue(
                                                                                        TargetingBe.Predicate.Value.newBuilder()
                                                                                                .setType(RANGE)
                                                                                                .setRange(
                                                                                                        TargetingBe.Predicate.Value.Range.newBuilder()
                                                                                                                .setBegin(1000)
                                                                                                                .setEnd(2000)
                                                                                                )
                                                                                )
                                                                )
                                                )
                                )
                );

        TargetingBe.TargetingBE targetingBE = builder.build();
        System.out.println(TextFormat.printToString(targetingBE));

        TargetingBe.TargetingDNF.Builder dnfBuilder = TargetingBe.TargetingDNF.newBuilder();


        BetreeParser betreeParser = new BetreeParser();
        betreeParser.convertToTargetingDNF(targetingBE, dnfBuilder);

        TargetingBe.TargetingDNF dnf = dnfBuilder.build();
        System.out.println(dnf);
        System.out.println("-----------------------------------------------");
        processBe(dnf, 1);

    }

    private void processBe(TargetingBe.TargetingDNF dnf, Integer localId) {
        Map<Integer,Boolean> ad_token_set = new HashMap<>();

        List<TargetingBe.TargetingDNF.Conjunction> conjunctionList = dnf.getConjunctionList();
        for (TargetingBe.TargetingDNF.Conjunction conj : conjunctionList) {

            String finger = calcConjFinger(conj);
            System.out.println(finger);
            TargetingIndexOuterClass.ConjunctionIndex conjIndex;
            if (conjFingerMap.containsKey(finger)) {
                Integer conjId = conjFingerMap.get(finger);
                conjIndex = conjIndexVec.get(conjId);
            } else {
                Integer conjId = conjIndexVec.size();
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
        }
    }

    private String calcConjFinger(TargetingBe.TargetingDNF.Conjunction conj) {
        byte[] data = conj.toByteArray();
        return DigestUtils.md5Hex(data);
    }

    private void handler(TargetingBe.TargetingBE targetingBE, TargetingBe.TargetingDNF.Builder dnfBuilder) {


    }
}
