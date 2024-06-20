package com.yc.biz;

import org.elasticsearch.client.RestHighLevelClient;

@FunctionalInterface
public interface ElasticSearchTask {

    void doSomething(RestHighLevelClient client) throws Exception;
}
