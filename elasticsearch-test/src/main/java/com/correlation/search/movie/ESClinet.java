package com.correlation.search.movie;

import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.function.Function;

public class ESClinet implements AutoCloseable {
    TransportClient client;

    public ESClinet(Settings settings, String hostname, int port) throws UnknownHostException {
        this.client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostname), port));
    }

    public IndexResponse createIndex(String index,String type){
        return client.prepareIndex(index, type).get();
    }


    public ActionFuture<BulkResponse> bluk(BulkRequest request) {
        return this.client.bulk(request);
    }

    public <R> R action(Function<TransportClient, R> clientFunction) {
        return clientFunction.apply(this.client);
    }

    public void close() {
        this.client.close();
    }
}
