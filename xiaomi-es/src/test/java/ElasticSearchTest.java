

import com.alibaba.fastjson.JSON;
import com.yc.ElasticSearchApp;
import com.yc.bean.Manager;
import com.yc.bean.Phone;
import com.yc.biz.ConnectElasticSearch;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
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
import org.elasticsearch.client.indices.*;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * ElasticSearch测试类
 */
@SpringBootTest(classes = ElasticSearchApp.class)
@RunWith(SpringRunner.class)
public class ElasticSearchTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;


    // 测试索引的创建， Request PUT liuyou_index
    @Test
    public void createIndex() throws IOException {
        //创建索引请求
        CreateIndexRequest request=new CreateIndexRequest("create");
        //客户端执行请求  请求后获得响应
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);


        //获取索引
        GetIndexRequest getIndexRequest=new GetIndexRequest("create");
        //查看索引是否存在
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);

        //删除索引
        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest();
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());

    }
    //删除索引
    @Test
    public void DeleteIndex() throws IOException {


        DeleteIndexRequest deleteIndexRequest=new DeleteIndexRequest("create");
        AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(delete.isAcknowledged());

    }

    //添加文档
    @Test
    public void addDocument() throws IOException {
        //创建对象
        Manager manager=new Manager(1,"肖子涵","123");
        IndexRequest indexRequest=new IndexRequest("test2");
        //规则  PUT  /test2/_doc/1
        indexRequest.id("1");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        //将数据类型放入请求                         JSON格式
        indexRequest.source(JSON.toJSONString(manager), XContentType.JSON);

        //客户端发送请求 获取响应结果
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());  //对应我们命令返回的状态结果
    }


    //判断文档是否存在
    @Test
    public void testGetDocumentExists() throws IOException {
        //获取文档 判断是否存在
        GetRequest getRequest=new GetRequest("test2","1");
        //不获取_source的上下文了
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
        /*-----------------------------------*/
        //获取文档信息

    }
    //获取文档的信息
    @Test
    public void testGetDocument() throws IOException {
        //获取文档 判断是否存在
        GetRequest getRequest=new GetRequest("test2","1");
        //不获取_source的上下文了
        GetResponse documentFields = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(documentFields.getSourceAsString());//打印文档内容
        System.out.println(documentFields); //返回的是全部内容 和命令一样
    }

    //更新文档
    @Test
    public void updateDocument() throws IOException {
        //获取文档 判断是否存在
        UpdateRequest updateRequest=new UpdateRequest("test2","1");
        Manager manager=new Manager(1,"肖逼崽子","666");
        //将JSON格式的对象添加
        updateRequest.doc(JSON.toJSONString(manager),XContentType.JSON);
        UpdateResponse update = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(update.status());
        restHighLevelClient.close();
    }

    //删除文档
    @Test
    public void DeleteDocument() throws IOException {
        DeleteRequest deleteRequest=new DeleteRequest("test2","1");
        deleteRequest.timeout("1s");

        DeleteResponse delete = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(delete.status());
        restHighLevelClient.close();
    }


    //批量插入数据  真实环境一般都是批量插入
    @Test
    public void BulkDocument() throws IOException {
        BulkRequest bulkRequest=new BulkRequest();
        bulkRequest.timeout("10s");   //数据越大最好设置越大
        ArrayList<Manager> list=new ArrayList<>();
        list.add(new Manager(2,"肖子涵","886"));
        list.add(new Manager(2,"凌翔","856"));
        list.add(new Manager(2,"王冰冰","999"));
        //批处理请求
        for (int i = 0; i < list.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("test2")
                    .id(""+(i+1))
                    .source(JSON.toJSONString(list.get(i)),XContentType.JSON));
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(bulk.hasFailures()); //是否失败  返回false 代表成功

    }
    // 查询
    // SearchRequest 搜索请求
    //  SearchSourceBuilder 条件构造
    // HighLightBuilder  构建高亮
    // TermQueryBuilder 精确查询
    //


    //搜索
    @Test
    public void testSearch() throws IOException {
        SearchRequest searchRequest=new SearchRequest("test2");
        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.highlighter(); //高亮
        //查询条件,可以使用QueryBuilders工具来实现
        //QueryBuilders.termQuery 精确
        //QueryBuilders.matchAllQuery()  匹配所有
        TermQueryBuilder termQueryBuilder= QueryBuilders.termQuery("username","肖");
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


    }


}
