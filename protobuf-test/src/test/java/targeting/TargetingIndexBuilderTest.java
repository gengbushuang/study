package targeting;

import match.engine.TargetingIndexBuilder;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static targeting.TargetingBe.LogicalOp.And;
import static targeting.TargetingBe.LogicalOp.Or;
import static targeting.TargetingBe.Predicate.Value.Type.RANGE;

public class TargetingIndexBuilderTest {

    @Test
    public void testIndex() {

        TargetingBe.TargetingBE.Builder builder1 = TargetingBe.TargetingBE.newBuilder()
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
                                                .setOp(And)
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
                                                                .setNot(true)
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


        TargetingBe.TargetingBE.Builder builder2 = TargetingBe.TargetingBE.newBuilder()
                .setAid(200)
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
                                                .setOp(And)
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
                                                                .setNot(true)
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


        List<TargetingBe.TargetingBE> beList = new ArrayList<>();
        beList.add(builder1.build());
        beList.add(builder2.build());

        TargetingIndexOuterClass.TargetingIndex.Builder indexB = TargetingIndexOuterClass.TargetingIndex.newBuilder();

        TargetingIndexBuilder indexBuilder = new TargetingIndexBuilder(indexB);
        indexBuilder.build(beList);

        System.out.println(indexB.build());

    }
}
