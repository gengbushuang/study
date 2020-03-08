import com.correlation.search.movie.JsonUtil;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsRequest;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class ESClient {

    TransportClient client;


    public ESClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "elasticsearch")
//                .put("client.transport.sniff", true)
                .build();
        this.client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.38.5"), 9300));
    }

    public void delete(String index, String type) {

        DeleteIndexRequestBuilder deleteIndexRequestBuilder = client.admin().indices().prepareDelete(index);
        DeleteIndexResponse deleteIndexResponse = deleteIndexRequestBuilder.get();
        System.out.println(deleteIndexResponse);
    }

    public <T> void bluk(String index, String type, Collection<T> beans) {
        BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
        for (T t : beans) {
            if (t instanceof String) {
                bulkRequestBuilder.add(client.prepareIndex(index, type).setSource((String) t, XContentType.JSON));
            } else {
                bulkRequestBuilder.add(client.prepareIndex(index, type).setSource(JsonUtil.obj2String(t), XContentType.JSON));
            }
        }
        bulkRequestBuilder.get();
    }


    public void index() throws IOException {
        //twitter是索引
        IndexResponse response = client.prepareIndex("twitter", "_doc", "1")
                .setSource(jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
                .get();
    }

    public void orderIndex(String index, String type, String json, XContentType xtype) {
        IndexResponse indexResponse = client.prepareIndex(index, type).setSource(json, xtype).get();
        System.out.println(indexResponse);
    }

    public void orderSearch(String index,String type,QueryBuilder queryBuilder, TermsAggregationBuilder termsAggregationBuilder) {
//        SearchResponse searchResponse = client.prepareSearch("nvwa-log-20200214").setFrom(1).setSize(10).get();
//        System.out.println(searchResponse);

        SearchRequestBuilder nvwa_report = client.prepareSearch(index).setTypes(type).setSize(0);
        if (queryBuilder != null) {
            nvwa_report.setQuery(queryBuilder);
        }
        if (termsAggregationBuilder != null) {
            nvwa_report.addAggregation(termsAggregationBuilder);
        }


        SearchResponse searchResponse = nvwa_report.get();
        System.out.println(searchResponse);
    }

    public void get() {
        GetResponse response = client.prepareGet("twitter", "_doc", "1").get();
        System.out.println(response);
    }

    public void search() {
        //curl 'localhost:9200/get-together/_search?from=10&size=10'
//        SearchResponse searchResponse = client.prepareSearch("get-together").setFrom(10).setSize(10).get();
//        System.out.println(searchResponse);

        //curl 'localhost:9200/get-together/_search?sort=date:asc&_source=title,date'
        //返回title和date的字段
//        SearchResponse date = client.prepareSearch("get-together").addSort("date", SortOrder.ASC).setFetchSource(new String[]{"title", "date"}, null).get();
//        System.out.println(date);

        //curl 'localhost:9200/get-together/_search?sort=date:asc&q=title:elasticsearch'
        //查询title包含elasticsearch文档
//        SearchResponse date = client.prepareSearch("get-together").addSort("date", SortOrder.ASC).setQuery(QueryBuilders.matchQuery("title", "elasticsearch")).get();
//        System.out.println(date);

        SearchResponse date = client.prepareSearch("get-together").addSort("date", SortOrder.ASC).setQuery(QueryBuilders.matchQuery("title", "elasticsearch")).get();
        System.out.println(date);
    }

    public void ttt() {
        IndicesAdminClient indices = client.admin().indices();
        try {
            GetSettingsResponse getSettingsResponse = indices.getSettings(new GetSettingsRequest()).get();
            System.out.println(getSettingsResponse.getIndexToSettings().values());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void b() {
        CreateIndexResponse createIndexResponse1 = client.admin()
                .indices()
                .prepareCreate("get-together")
                .setSettings("{\"number_of_shards\":2,\"number_of_replicas\":1,\"index\":{\"analysis\":{\"analyzer\":{\"myCustomAnalyzer\":{\"type\":\"custom\",\"tokenizer\":\"myCustomTokenizer\",\"filter\":[\"myCustomFilter1\",\"myCustomFilter2\"],\"char_filter\":[\"myCustomCharFilter\"]}},\"tokenizer\":{\"myCustomTokenizer\":{\"type\":\"letter\"},\"myCustomNGramTokenizer\":{\"type\":\"ngram\",\"min_gram\":2,\"max_gram\":3}},\"filter\":{\"myCustomFilter1\":{\"type\":\"lowercase\"},\"myCustomFilter2\":{\"type\":\"kstem\"}},\"char_filter\":{\"myCustomCharFilter\":{\"type\":\"mapping\",\"mappings\":[\"ph=>f\",\" u => you \",\"ES=>Elasticsearch\"]}}}}}", XContentType.JSON)
                .addMapping("group", "{\"group\":{\"_source\":{\"enabled\":true},\"_all\":{\"enabled\":true},\"properties\":{\"organizer\":{\"type\":\"text\"},\"name\":{\"type\":\"text\"},\"description\":{\"type\":\"text\",\"term_vector\":\"with_positions_offsets\"},\"created_on\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd\"},\"tags\":{\"type\":\"text\",\"index\":true,\"fields\":{\"verbatim\":{\"type\":\"text\",\"index\":false}}},\"members\":{\"type\":\"text\"},\"locationname\":{\"type\":\"text\"}}}}", XContentType.JSON)
                .addMapping("event", "{\"event\":{\"_source\":{\"enabled\":true},\"_all\":{\"enabled\":false},\"_parent\":{\"type\":\"group\"},\"properties\":{\"host\":{\"type\":\"text\"},\"title\":{\"type\":\"text\"},\"description\":{\"type\":\"text\",\"term_vector\":\"with_positions_offsets\"},\"attendees\":{\"type\":\"text\"},\"date\":{\"type\":\"date\",\"format\":\"date_hour_minute\"},\"reviews\":{\"type\":\"integer\",\"null_value\":0},\"location\":{\"type\":\"object\",\"properties\":{\"name\":{\"type\":\"text\"},\"geolocation\":{\"type\":\"geo_point\"}}}}}}", XContentType.JSON)
                .get();

        System.out.println(createIndexResponse1);
    }

    public void delete() {
        client.prepareDelete().setIndex("get-together").get();
    }

    public void close() {
        client.close();
    }
}
