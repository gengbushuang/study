package com.retrieval.indexer.section;

import com.proto.indexer.Indexer;
import com.retrieval.indexer.AbsIndexer;
import com.retrieval.indexer.Token;
import com.retrieval.indexer.basic.TokenLong;
import com.retrieval.model.DocidNode;
import com.retrieval.util.PostingList;
import com.retrieval.util.PostingListIterator;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 区间索引
 */
public class IndexerSection extends AbsIndexer {

    private Map<TokenSection, Integer> rmapping = new HashMap<>();

    private ConcurrentSkipListMap<Integer, PostingList<DocidNode>> table = new ConcurrentSkipListMap<>();

    private SectionCoverIndex sectionCoverIndex;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public IndexerSection(String name) {
        super(name);
    }

    @Override
    protected void add(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId) {
        List<Token> tokens = parseConjValue(value);
        if (tokens.isEmpty()) {

            return;
        }
        for (Token token : tokens) {
            buildToken(token, uniqueId, isNot);
        }

        SectionCoverIndex tmp = new SectionCoverIndex();
        BuildIntervalCoverIndex(rmapping, tmp);

        readWriteLock.writeLock().lock();
        this.sectionCoverIndex = tmp;
        readWriteLock.writeLock().unlock();

    }

    private void buildToken(Token token, long uniqueId, boolean isNot) {
        boolean containsKey = rmapping.containsKey(token);
        Integer tokenId;
        if (containsKey) {
            tokenId = rmapping.get(token);
        } else {
            tokenId = Integer.valueOf(table.size());
            rmapping.put((TokenSection) token, tokenId);
            table.put(tokenId, new PostingList<DocidNode>());
        }
        table.get(tokenId).add(new DocidNode(uniqueId, isNot));
    }


    @Override
    protected boolean delete(com.proto.indexer.Indexer.AssignValue value, boolean isNot, long uniqueId) {
        List<Token> tokens = parseConjValue(value);
        if (tokens.isEmpty()) {

            return false;
        }

        for (Token token : tokens) {
            if (!rmapping.containsKey(token)) {
                continue;
            }
            delete((TokenSection) token, new DocidNode(uniqueId, isNot));
        }

        return false;
    }

    private void delete(TokenSection token, DocidNode docidNode) {
        Integer integer = rmapping.get(token);

        Map<Long, Integer> pMapIndex = new HashMap<>();
        List<PointInfo> pointInfos = sectionCoverIndex.getPointInfos();

        for (int i = 0; i < pointInfos.size(); i++) {
            PointInfo info = pointInfos.get(i);
            pMapIndex.put(Long.valueOf(info.getPoint()), Integer.valueOf(i));
        }

        long left = token.getLeft();
        long right = token.getRight();
        Integer b = pMapIndex.get(left);
        Integer e = pMapIndex.get(right);

        readWriteLock.writeLock().lock();
        try {
            for (int i = b.intValue(); i < e.intValue(); i++) {
                sectionCoverIndex.getPointInfos().get(Integer.valueOf(i)).delete(integer);
            }
        } finally {
            readWriteLock.writeLock().unlock();
        }

        if (table.containsKey(integer)) {
            table.get(integer).delete(docidNode);
        }
    }


    @Override
    protected List<Token> parseConjValue(com.proto.indexer.Indexer.AssignValue value) {
        List<Token> tokens;
        if (value.getIntervalsCount() > 0) {
            tokens = ConvToken(this.getName(), value.getIntervalsList());
        } else {
            tokens = Collections.EMPTY_LIST;
        }

        return tokens;
    }

    @Override
    protected PostingListIterator getPostingLists(Token token) {
        if (token instanceof TokenLong) {
            readWriteLock.readLock().lock();
            try {
                TokenLong tokenLong = (TokenLong) token;
                Set<Integer> ids = search(sectionCoverIndex, tokenLong.getVal());
                if (ids.isEmpty()) {
                    return null;
                }
                PostingList<DocidNode> postingList = new PostingList<>();
                for (Integer id : ids) {
                    PostingList<DocidNode> docidNodes = table.get(id);
                    if (docidNodes != null) {
                        postingList.merge(docidNodes);
                    }
                }
                return postingList.iterator();
            } finally {
                readWriteLock.readLock().unlock();
            }
        }
        return null;
    }

    private Set<Integer> search(SectionCoverIndex sectionCoverIndex, long point) {
        List<PointInfo> pointInfos = sectionCoverIndex.getPointInfos();
        int low = 0;
        int high = pointInfos.size();
        for (; low < high; ) {
            int mid = (low + high) >> 1;
            if (pointInfos.get(mid).getPoint() < point) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        int index = low;
        if (pointInfos.get(low).getPoint() != point) {
            index--;
        }
        return pointInfos.get(index).getIds();
    }

    private void BuildIntervalCoverIndex(Map<TokenSection, Integer> rmapping, SectionCoverIndex sectionCoverIndex) {
        Map<Long, Boolean> existMap = new HashMap<>();
        for (TokenSection section : rmapping.keySet()) {
            existMap.put(section.getLeft(), true);
            existMap.put(section.getRight(), true);
        }

        for (Long p : existMap.keySet()) {
            PointInfo pointInfo = new PointInfo(p.longValue());
            sectionCoverIndex.add(pointInfo);
        }

        sectionCoverIndex.getPointInfos().sort(new Comparator<PointInfo>() {
            @Override
            public int compare(PointInfo o1, PointInfo o2) {
                return (int) (o1.getPoint() - o2.getPoint());
            }
        });

        Map<Long, Integer> pMapIndex = new HashMap<>();
        List<PointInfo> pointInfos = sectionCoverIndex.getPointInfos();
        for (int i = 0; i < pointInfos.size(); i++) {
            PointInfo info = pointInfos.get(i);
            pMapIndex.put(Long.valueOf(info.getPoint()), Integer.valueOf(i));
        }

        for (Map.Entry<TokenSection, Integer> entry : rmapping.entrySet()) {
            Long bR = Long.valueOf(entry.getKey().getLeft());
            Long eR = Long.valueOf(entry.getKey().getRight());
            Integer b = pMapIndex.get(bR);
            Integer e = pMapIndex.get(eR);
            for (int i = b.intValue(); i < e.intValue(); i++) {
                sectionCoverIndex.getPointInfos().get(Integer.valueOf(i)).add(entry.getValue());
            }
        }
    }

    private List<Token> ConvToken(String name, List<Indexer.Interval> intervalsList) {
        List<Token> tokens = new ArrayList<>(intervalsList.size());
        for (Indexer.Interval interval : intervalsList) {
            tokens.add(new TokenSection(name, interval.getBegin(), interval.getEnd()));
        }
        return tokens;
    }
}
