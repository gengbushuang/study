import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;

import java.net.UnknownHostException;

public class SearchTest {

    public static void main(String[] args) throws UnknownHostException {
        String index = "nvwa-log-20200215";
        String type = "nvwa_report";
        ESClient client = new ESClient();

//        client.ttt();
//        TermQueryBuilder cre_day = QueryBuilders.termQuery("cre_day", 20200113);
//        RangeQueryBuilder cre_day1 = QueryBuilders.rangeQuery("cre_day").gte(20191108).lte(20200115);
//        TermsAggregationBuilder cre_day_count = AggregationBuilders.terms("cre_day_count").field("cre_day");
//
//        TermsAggregationBuilder flow_id_count = AggregationBuilders.terms("flow_id_count").field("flow_id.keyword");
//        flow_id_count
//                .subAggregation(
//                        AggregationBuilders.terms("cre_day_count")
//                                .field("cre_day").order(Terms.Order.term(false))
//                                .subAggregation(AggregationBuilders.sum("exp_count").field("exp"))
//                                .subAggregation(AggregationBuilders.sum("clk_count").field("clk"))
//
//                )
//
//        ;
//        client.orderSearch(null,flow_id_count);
//        TermsAggregationBuilder order_id_count = AggregationBuilders.terms("order_id_count").field("order_id");
//        order_id_count.order(Terms.Order.term(false));
//        TermsAggregationBuilder cre_day_count = AggregationBuilders.terms("cre_day_count").field("cre_day");
//        cre_day_count.order(Terms.Order.term(false));
//        order_id_count.subAggregation(cre_day_count);
//
//        cre_day_count
//                .subAggregation(AggregationBuilders.sum("pv_exp_count").field("pv_exp"))
//                .subAggregation(AggregationBuilders.sum("uv_exp_count").field("uv_exp"))
//                .subAggregation(AggregationBuilders.sum("ip_exp_count").field("ip_exp"))
//                .subAggregation(AggregationBuilders.sum("pv_clk_count").field("pv_clk"))
//                .subAggregation(AggregationBuilders.sum("uv_clk_count").field("uv_clk"))
//                .subAggregation(AggregationBuilders.sum("ip_clk_count").field("ip_clk"));
//
//        order_id_count.size(100);

        TermsAggregationBuilder unitId_count = AggregationBuilders.terms("unitId_count").field("unitId");
        unitId_count.order(Terms.Order.term(false));

        TermsAggregationBuilder cid_count = AggregationBuilders.terms("cid_count").field("cid");
        cid_count.order(Terms.Order.term(false));

        unitId_count.subAggregation(cid_count);

        client.orderSearch(index,type,null,unitId_count);
        client.close();
    }
}
