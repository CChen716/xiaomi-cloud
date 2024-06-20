package com.Redis;

import com.yc.ImgApp;
import com.yc.util.CacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**测试Redis+Lua脚本 解决并发扣减库存问题
 * @author 12547
 * @version 1.0
 * @Date 2024/3/6 18:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class TestRedisLua {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Autowired
    private CacheService cacheService;


    private static final String key="seckill:goodsStock:123";  //--商品id

    private static  final String num="1";  //测试数据订购数量

    private static final String errNum="1001";  //测试失败情况


    private static final DefaultRedisScript<String> SECKILL_KEY;

    static {
        SECKILL_KEY=new DefaultRedisScript<>();
        SECKILL_KEY.setLocation(new ClassPathResource("seckill.lua"));
        SECKILL_KEY.setResultType(String.class);//返回结果类型
    }
    //添加测试数据
    @Test
    public void testAddHash(){

        Map<String,Object> map=new HashMap<>();
        map.put("totalCount","100");
        map.put("seckillCount","0");
        redisTemplate.opsForHash().putAll(key,map);
    }


    /**
     * 通过文件加载lua脚本
     */
    @Test
    public void testRedisLuaResource(){
        //key没问题 addCap也没问题
        String result = redisTemplate.execute(SECKILL_KEY, Collections.singletonList(key), num);
        System.out.println(result);
    }
    /**秒杀下单场景的Lua脚本Demo
     *
     */
    @Test
    public void testRedisLua(){
        StringBuilder sb=new StringBuilder();
        //传入的参数应该是商品的哈希key
        //哈希结构key-商品id
        //totalCount   100  总库存
        //seckillCount  0     已经售出数量
        sb.append("local resultFlag=\"0\"\n");
        //传入的参数key   --对应商品id key
        sb.append("local key=KEYS[1]\n");
        //传入的参数arg   --对应订购的数量
        sb.append("local addCap=tonumber(ARGV[1])\n");
        //先将key进行查询判断库存是否充裕
        sb.append("local phoneInfo=redis.call(\"HMGET\",key,\"totalCount\",\"seckillCount\")\n");
        sb.append("local total=tonumber(phoneInfo[1])\n");//得到totalCount 总库存数
        sb.append("local alsell=tonumber(phoneInfo[2])\n");//得到seckillCount 已经售出的数量
        //如果没有值则返回 “0”
        sb.append("if not total then\n");
        sb.append("return resultFlag\n");
        sb.append("end\n");
        //如果库存数量大于已售出数量加需要订购的数量（代表还有货）则将已售出数量增加订购数量并返回
        sb.append("if total >=alsell + addCap then\n");
        //将售出数量进行增加(订购数量)
        sb.append("local ret=redis.call(\"HINCRBY\",key,\"seckillCount\",addCap)\n");
        sb.append("return tostring(ret)\n");
        sb.append("end\n");
        //如果没货返回 "0"
        sb.append("return resultFlag\n");
        RedisScript<String> redisScript=new DefaultRedisScript<>(sb.toString(),String.class);

        String result = redisTemplate.execute(redisScript, Arrays.asList(key), num);
        System.out.println(result);
    }



}
