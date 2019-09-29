package targeting;

import com.google.protobuf.TextFormat;
import org.junit.Test;

import static targeting.TargetingBe.LogicalOp.And;
import static targeting.TargetingBe.LogicalOp.Or;
import static targeting.TargetingBe.Predicate.Value.Type.RANGE;

public class TargetingBeTest {

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

        handler(targetingBE, dnfBuilder);
    }

    private void handler(TargetingBe.TargetingBE targetingBE, TargetingBe.TargetingDNF.Builder dnfBuilder) {
        TargetingBe.BETree betree = targetingBE.getBetree();
        boolean not = betree.getNot();

        if (betree.hasPredicate()) {
            TargetingBe.Predicate.Builder predicateBuilder = betree.getPredicate().toBuilder();
            boolean predicateNot = betree.getPredicate().getNot();
            if (not) {
                predicateNot = !predicateNot;
            }
            predicateBuilder.setNot(predicateNot);

        }

    }
}
