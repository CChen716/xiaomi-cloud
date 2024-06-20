package com.yc.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yc.entity.EsPhone;
import com.yc.config.EsConfig;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Es操作业务类
 * 2023.12.11
 * CG
 */
@Component
public class EsService {

    //需要包装数据返回的使用
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ObjectMapper objectMapper;  //JSON转对象用


    /**
     * 创建索引
     * @param index 索引名称
     */
    public void createIndex(String index){
        //lambda写法
        ConnectElasticSearch.connect(client -> {
            CreateIndexRequest createIndexRequest=new CreateIndexRequest(index);
            CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            System.out.println(createIndexResponse);
        });
    }

    /**
     * 查看索引是否存在
     * @param index 索引名
     */
    public void checkIndex(String index){
        ConnectElasticSearch.connect(client -> {
            GetIndexRequest getIndexRequest=new GetIndexRequest(index);
            boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            System.out.println("索引是否存在："+exists);
        });
    }

    /**
     * 删除索引
     * @param index 索引名
     */

    public void deleteIndex(String index){
        ConnectElasticSearch.connect(client -> {
            DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest(index);
            AcknowledgedResponse delete = client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            System.out.println("索引删除是否成功："+delete.isAcknowledged());
        });
    }


    /**
     * 初始索引中添加单个文档
     * @param object 类对象
     * @param id    对象id
     * @param index 索引名
     */
    public void addDocument(Object object,String id,String index){
        ConnectElasticSearch.connect(client -> {
            IndexRequest indexRequest=new IndexRequest(index);
            //单个文档超时时间
            indexRequest.timeout(EsConfig.DTimeOut);
            //将对象转为JSON添加
            indexRequest.id(id).source(JSON.toJSONString(object), XContentType.JSON);
            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("状态码："+indexResponse.status());
            System.out.println(indexResponse.toString());
        });

    }

    /**
     * /初始索引中批量添加文档数据  不强制要求id值
     * @param objectList 对象集合
     * @param index 索引名
     */
    public void bulkDocument(List<Object> objectList,String index){
        ConnectElasticSearch.connect(client -> {
            BulkRequest bulkRequest=new BulkRequest(index);
            bulkRequest.timeout(EsConfig.DSTimeOut);
            for (int i = 0; i < objectList.size(); i++) {
                bulkRequest.add(
                        new IndexRequest(index)
                                .id(""+(i+1))
                                .source(JSON.toJSONString(objectList.get(i)),XContentType.JSON)
                );
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("添加是否失败=" + bulkResponse.hasFailures()); //返回false代表成功
        });
    }

    /**
     * 批量插入升级版,文档id值和对象id值一一对应(便于后续删改操作)
     * 其中的id值--指的是在es索引中文档的id值  相当于数据库中每一行数据的id值  需要将这两个id值设为同一个
     * @param map 带有id的对象map
     * @param index 索引名
     */
    public void bulkByIdDocument(Map<String,Object> map, String index){
        ConnectElasticSearch.connect(client -> {
            BulkRequest bulkRequest=new BulkRequest(index);
            bulkRequest.timeout(EsConfig.DSTimeOut);
            for (String id : map.keySet()) {
                Object o = map.get(id);
                bulkRequest.add(
                        new IndexRequest(index)
                        .id(id)
                        .source(JSON.toJSONString(map.get(id)),XContentType.JSON)
                );
            }
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("添加是否失败=" + bulkResponse.hasFailures()); //返回false代表成功
        });
    }

    /**
     * 索引中批量删除文档数据
     * @param list 需要删除的对象的id值的集合
     * @param index 索引名
     */
    public void bulkDeleteDocument(List<String> list,String index){
        ConnectElasticSearch.connect(client -> {
            BulkRequest bulkRequest=new BulkRequest(index);
            for (int i = 0; i < list.size(); i++) {
                bulkRequest.add(
                        new DeleteRequest(list.get(i))
                );
            }
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("删除成功："+!bulk.hasFailures());
        });

    }

    /**
     * 判断文档是否存在
     * @param id 文档id
     * @param index 索引名
     */
    public void existsDocument(String id,String index){

        ConnectElasticSearch.connect(client -> {
            GetRequest getRequest=new GetRequest(index,id);
            getRequest.fetchSourceContext(new FetchSourceContext(false));
//        设置获取文档时是否返回_source字段的内容。在Elasticsearch中，每个文档都有一个_source字段，其中包含了文档的原始JSON格式内容。
//        通过fetchSourceContext方法可以设置是否返回_source字段的内容。在这里，new FetchSourceContext(false)表示不返回_source字段的内容。
//        这样做可以在查询时节省网络带宽和内存
            getRequest.storedFields("_none_");
//        设置获取文档时是否返回存储字段的内容。在Elasticsearch中，除了_source字段外，还可以选择将文档的特定字段存储起来。
//        通过storedFields方法可以设置是否返回存储字段的内容。在这里，"_none_"表示不返回任何存储字段的内容，同样可以节省资源
            boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);
            System.out.println("文档是否存在："+exists);
        });
    }

    /**
     * 更新单个文档数据
     * @param id 文档id
     * @param index 索引名
     * @param object 新数据对象
     */
    public void updateDocument(String id,String index,Object object){
        ConnectElasticSearch.connect(client -> {
            UpdateRequest updateRequest=new UpdateRequest(index,id);
            updateRequest.doc(JSON.toJSONString(object),XContentType.JSON);
            UpdateResponse update = client.update(updateRequest, RequestOptions.DEFAULT);
            System.out.println(update.status());
        });
    }


    /**
     * 查询文档数据并且以对象形式返回
     * @param id 文档id
     * @param clazz 文档数据对应类
     * @param index 索引名
     * @param <V> 类类型
     * @return 直接返回对应的对象不需要再外面转换
     */
    public <V> V getDocument(String id,Class<V> clazz,String index){
        V object=null;
        try {
            GetRequest getRequest=new GetRequest(index,id);
            GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            String value=documentFields.getSourceAsString();
            object= JSONObject.parseObject(value,clazz);
            System.out.println(object);  //此处直接返回的是一个对象
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object; //如若没查到 返回的是null
    }


//---------------------------------------------分割线-----------------------------------------------------------------------------------------------------------------

    /**
     *
     * @param keyword 搜索关键字
     * @return 返回搜索集合
     * @throws IOException
     */
    //搜索业务       *核心业务  直接返回需要的数据
    public  List<EsPhone> search(String keyword,Integer pageSize,Integer pageNo) throws IOException {
        SearchRequest searchRequest=new SearchRequest(EsConfig.index);
        SearchSourceBuilder sourceBuilder=new SearchSourceBuilder();

//      //  精准匹配          TODO 未知问题无法紧确搜索                     ES中的数据列, 搜索条件
//        TermQueryBuilder termQueryBuilder= QueryBuilders.termQuery("name",keyword);
//        sourceBuilder.query(termQueryBuilder);
//        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //组合查询
        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();
        //必须包含 --商品名
        boolQueryBuilder.must(QueryBuilders.matchQuery("name",keyword));
        //分页
        //当前页起始索引（第一条数据的索引号）
        sourceBuilder.from(pageNo);
        //每页显示多少条
        sourceBuilder.size(pageSize);


        //可能包含 --品牌名
      //  boolQueryBuilder.should(QueryBuilders.matchQuery("brand",keyword));
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        //执行搜索
        searchRequest.source(sourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<EsPhone> list=new ArrayList<>();

        for (SearchHit documentFields : search.getHits().getHits()) {
        //使用objectMapper将每一个文档JSON数据转为需要的对象
        list.add(objectMapper.readValue(documentFields.getSourceAsString(),EsPhone.class));
        }
        restHighLevelClient.close();
        return list;
    }

}
