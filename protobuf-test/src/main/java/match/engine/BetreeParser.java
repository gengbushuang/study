package match.engine;

import targeting.TargetingBe;

import java.util.*;
import java.util.function.Consumer;

public class BetreeParser {
    private final static int maxPredicateSize = 1024;
    private final static int ParseTypeDNF = 1;
    private final static int ParseTypeCNF = 2;

    ParseResult result = new ParseResult();
    List<TargetingBe.Predicate> predicateVec = new ArrayList<>();


    class ParseResult implements Iterable<BitSet> {
        List<BitSet> bitSetList = new ArrayList<>();

        public void add(BitSet bitSet) {
            bitSetList.add(bitSet);
        }

        public int size() {
            return bitSetList.size();
        }

        @Override
        public Iterator<BitSet> iterator() {
            return bitSetList.iterator();
        }

        @Override
        public String toString() {
            return "ParseResult{" +
                    "bitSetList=" + bitSetList +
                    '}';
        }
    }

    public boolean convertToTargetingDNF(TargetingBe.TargetingBE ad, TargetingBe.TargetingDNF.Builder dnf) {

        parse(ad.getBetree(), ParseTypeDNF);
        dnf.setAid(ad.getAid());

        for (BitSet bset : result) {
            TargetingBe.TargetingDNF.Conjunction.Builder conjBuilder = TargetingBe.TargetingDNF.Conjunction.newBuilder();
            for (int i = bset.nextSetBit(0); i >= 0; i = bset.nextSetBit(i + 1)) {
                TargetingBe.Predicate predicate = predicateVec.get(i);
                conjBuilder.addPredicate(predicate);
            }
            dnf.addConjunction(conjBuilder);
        }
        return true;
    }

    void parse(TargetingBe.BETree betree, int parseType) {
        result = parseInternal(betree, parseType, false);
    }

    ParseResult parseInternal(TargetingBe.BETree betree, int parseType, boolean not) {
        boolean localNot = betree.getNot();
        if (not) {
            localNot = !localNot;
        }
        ParseResult result = new ParseResult();
        if (betree.hasPredicate()) {
            TargetingBe.Predicate.Builder predicateBuilder = TargetingBe.Predicate.newBuilder(betree.getPredicate());
            boolean predicateNot = betree.getPredicate().getNot();
            if (localNot) {
                predicateNot = !predicateNot;
            }
            predicateBuilder.setNot(predicateNot);
            int index = predicateVec.size();
            predicateVec.add(predicateBuilder.build());

            BitSet bset = new BitSet(maxPredicateSize);
            bset.set(index);
            result.add(bset);
        }

        TargetingBe.LogicalOp logicalOp = betree.getOp();
        if (localNot) {
            if (logicalOp == TargetingBe.LogicalOp.And) {
                logicalOp = TargetingBe.LogicalOp.Or;
            } else {
                logicalOp = TargetingBe.LogicalOp.And;
            }
        }

        List<TargetingBe.BETree> betreeList = betree.getBetreeList();
        for (TargetingBe.BETree subtree : betreeList) {
            ParseResult subRes = parseInternal(subtree, parseType, localNot);
            if (result.size() == 0) {
                result = subRes;
                continue;
            }
            if (needMultiply(parseType, logicalOp)) {
                result = Mutiply(result, subRes);
            } else if (needAdd(parseType, logicalOp)) {
                result = Add(result, subRes);
            }
        }

        return result;
    }

    private ParseResult Add(ParseResult resa, ParseResult resb) {
        for (BitSet b : resb) {
            resa.add(b);
        }
        return resa;
    }

    private boolean needAdd(int parseType, TargetingBe.LogicalOp op) {
        if (parseType == ParseTypeDNF && op == TargetingBe.LogicalOp.Or) {
            return true;
        }

        if (parseType == ParseTypeCNF && op == TargetingBe.LogicalOp.And) {
            return true;
        }
        return false;
    }

    private ParseResult Mutiply(ParseResult resa, ParseResult resb) {
        ParseResult tmp = new ParseResult();
        System.out.println(resa);
        System.out.println(resb);
        for (BitSet a : resa) {
            for (BitSet b : resb) {
                b.or(a);
//                a.or(b);
                tmp.add(b);
            }
        }
        return tmp;
    }


    private boolean needMultiply(int parseType, TargetingBe.LogicalOp op) {
        if (parseType == ParseTypeDNF && op == TargetingBe.LogicalOp.And) {
            return true;
        }

        if (parseType == ParseTypeCNF && op == TargetingBe.LogicalOp.Or) {
            return true;
        }
        return false;
    }
}
