package com.correlation.search.movie;


import com.correlation.search.movie.bean.MovieBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.lucene.search.Explanation;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * debug模式检查
 * curl 'http://localhost:9200/tmdb/movie/_validate/query?explain' -d '
 * {"query":{"multi_match":{"query":"basketball with cartoon aliens","fields":["title^10","overview"]}}}
 * '
 * <p>
 * 词检查
 * curl 'http://localhost:9200/tmdb/_analyze?format=yaml' -d '
 * {"analyzer":"standard","text":"Fire with Fire"}
 * '
 */
public class MovieClient {

    ESClinet clinet;

    public MovieClient(String hostname, int port) throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch").build();
        this.clinet = new ESClinet(settings, hostname, port);
    }

    public void delete(String index, String type, String id) {
        DeleteResponse deleteResponse = clinet.action(esclient -> {
            return esclient.prepareDelete().setIndex(index).setType(type).setId(id).get();
        });
        System.out.println(deleteResponse);
    }

    public void delete(String index) {
        DeleteIndexResponse deleteIndexResponse = clinet.action(esclient -> {
            return esclient.admin().indices().prepareDelete(index).get();
        });
        System.out.println(deleteIndexResponse);
    }


    public void index(String index, String settings, Map<String, String> mappings) {
        CreateIndexResponse createIndexResponse = clinet.action(esclient -> {
            CreateIndexRequestBuilder builder = esclient.admin().indices().prepareCreate(index);
            if (settings != null) {
                builder.setSettings(settings, XContentType.JSON);
            }
            if (mappings != null && !mappings.isEmpty()) {
                for (Map.Entry<String, String> entry : mappings.entrySet()) {
                    builder.addMapping(entry.getKey(), entry.getValue(), XContentType.JSON);
                }
            }
            return builder.get();
        });
        System.out.println(createIndexResponse);
    }

    public void bulk(String index, String type, Collection<MovieBean> beans) {
        BulkResponse bulkResponse = clinet.action(esclient -> {
            BulkRequestBuilder bulkRequestBuilder = esclient.prepareBulk();
            for (MovieBean movieBean : beans) {
                bulkRequestBuilder.add(
                        esclient.prepareIndex(index, type, String.valueOf(movieBean.getId()))
                                .setSource(JsonUtil.obj2String(movieBean), XContentType.JSON)
                );
            }
            return bulkRequestBuilder.get();
        });
    }

    public SearchResponse search(String index, String type, QueryBuilder query, boolean explain) {
        SearchResponse searchResponse = clinet.action(esclient -> {
            return esclient.prepareSearch(index)
                    .setTypes(type)
                    .setQuery(query)
                    .setFrom(0)
                    .setSize(100)
                    .setExplain(explain)
                    .get();
        });
        return searchResponse;
    }

    public void close() {
        if (clinet != null) {
            clinet.close();
        }
    }


    public void testShowSearch(SearchResponse searchResponse, boolean explain) {
        SearchHits hits = searchResponse.getHits();
        SearchHit[] hitsHits = hits.getHits();
        SearchHit searchHit;
        for (int i = 0; i < hitsHits.length; i++) {
            searchHit = hitsHits[i];
            if (explain) {
                System.out.println(searchHit.getSource().get("title"));
                System.out.println(searchHit.getExplanation().getDescription());
                System.out.println(Arrays.toString(searchHit.getExplanation().getDetails()));
            } else {
                System.out.printf("%s\t%s\t\t%s|\t%s\n", (i + 1), searchHit.getScore(), searchHit.getSource().get("title"), searchHit.getSource().get("overview"));
            }
        }
    }


    public static void main(String[] args) throws IOException {
        String index = "tmdb";
        String type = "movie";

        MovieClient movieClient = new MovieClient("192.168.38.5", 9300);
//        delete操作
//        movieClient.delete(index);
//        index操作
//        Map<String,String> mappings = new HashMap<>();
//        mappings.put("movie","{\"movie\":{\"properties\":{\"title\":{\"type\":\"text\",\"analyzer\":\"my_english_analyzer\"},\"overview\":{\"type\":\"text\",\"analyzer\":\"my_english_analyzer\"}}}}");
//        movieClient.index(index,"{\"number_of_shards\":1,\"number_of_replicas\":0,\"analysis\":{\"filter\":{\"english_stop\":{\"type\":\"stop\",\"stopwords\":\"_english_\"},\"english_stemmer\":{\"type\":\"stemmer\",\"language\":\"english\"},\"english_possessive_stemmer\":{\"type\":\"stemmer\",\"language\":\"possessive_english\"}},\"analyzer\":{\"my_english_analyzer\":{\"tokenizer\":\"standard\",\"filter\":[\"english_possessive_stemmer\",\"lowercase\",\"english_stop\",\"english_stemmer\"]}}}}",mappings);

        //add操作
//        byte[] bytes = Files.readAllBytes(Paths.get("C:\\Users\\gbs\\git\\study\\elasticsearch-test\\src\\date\\tmdb.json"));
//        String json = new String(bytes);
//        Map<String, MovieBean> movieBeanMap = JsonUtil.string2Obj(json, new TypeReference<Map<String, MovieBean>>() {
//        });
//        Collection<MovieBean> beans = movieBeanMap.values();
//        movieClient.bulk(index,type,beans);

        //search操作
        MultiMatchQueryBuilder multiMatchQuery = QueryBuilders
                .multiMatchQuery("mr. weirdlove: don't worry, I'm learning to start loving bombs", "title");
//        multiMatchQuery.field("title", 0.1F);
        boolean explain = false;
        SearchResponse search = movieClient.search(index, type, multiMatchQuery, explain);
        movieClient.testShowSearch(search, explain);
        movieClient.close();
    }
}
