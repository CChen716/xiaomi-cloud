import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.util.Util;
import com.yc.ElasticSearchApp;
import com.yc.bean.Manager;
import com.yc.biz.EsService;
import com.yc.entity.EsPhone;
import com.yc.config.EsConfig;
import com.yc.util.EsDemo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = ElasticSearchApp.class)
@RunWith(SpringRunner.class)
public class EsServiceTest {

    @Autowired
    private EsService esService;

    @Autowired
    private EsDemo esDemo;

    //创建索引
    @Test
    public void test1(){
        String index="xiaomi-phone";
        esService.createIndex(index);
    }
    //检查索引
    @Test
    public void test2(){
        String index="testcreate";
        esService.checkIndex(index);
    }
    //删除索引
    @Test
    public void test3(){
        String index="testcreate";
        esService.deleteIndex(index);
    }






    //添加单个文档
    @Test
    public void testAdd(){
        Manager manager=new Manager(1,"肖子涵","222");
        esService.addDocument(manager,"1", EsConfig.index);
    }
    //批量添加文档
    @Test
    public void testBulk(){




        Manager manager=new Manager(1,"肖子涵","222");
        Manager manager2=new Manager(1,"小比崽子","333");
        Manager manager3=new Manager(1,"小兔崽子","444");
        List<Object> list=new ArrayList<>();
        list.add(manager);
        list.add(manager2);
        list.add(manager3);
        esService.bulkDocument(list,EsConfig.index);
    }

    //查看文档是否存在
    @Test
    public void testCheck(){
        esService.existsDocument("1",EsConfig.index);
    }
    //获取文档信息
    @Test
    public void testGet(){

        Manager manager=esService.getDocument("4",Manager.class,EsConfig.index);
        System.out.println(manager);
    }


    //测试id同步的批量
    @Test
    public void testID(){
        Map<String,Object> map=new HashMap<>();
        Manager manager=new Manager(66,"肖子涵","222");
        Manager manager2=new Manager(77,"小比崽子","333");
        Manager manager3=new Manager(88,"小兔崽子","444");
        List<Manager> list=new ArrayList<>();
        list.add(manager);
        list.add(manager2);
        list.add(manager3);
        for (Manager manager1 : list) {
            map.put(String.valueOf(manager1.getId()),manager1);
        }//将所有对象添加进去
        esService.bulkByIdDocument(map,EsConfig.index);

    }

    //测试搜索
    @Test
    public void testSearch() throws IOException {
        String key1="vivo";
        String key2="华为";
        String key3="苹果";
        String key4="oppo";
        String key5="华为 P50";
        String key6="P50";
        String key7="OPPO";

     //  List<EsPhone> search = esService.search(key7);

        //System.out.println(search.toString());



    }






    //将数据导入es的demo
   @Test
    public void addPhone(){
       // esService.deleteIndex(EsConfig.index);//删除原有
      //  esService.createIndex(EsConfig.index);//创建新的
        esDemo.addPhone();
    }

}
