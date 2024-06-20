package com.yc.biz;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public class ConnectElasticSearch {


    public static void connect(ElasticSearchTask task){
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1",9200,"http")));
            try {
                task.doSomething(client);
                client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
