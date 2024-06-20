package com.yc.biz;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.ByteString;
import com.yc.config.MqConstants;
import com.yc.mqlistener.RedisDataListenerService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**Canal监听类
 * 实现CommandLineRunner接口 在启动app后会立即执行该方法
 * 而此处是通过while(true)方式 即会一直运行
 * @author 12547
 * @version 1.0
 * @Date 2024/3/19 20:44
 */
@Component
public class CanalService {


    @Autowired
    private RedisDataListenerService redisDataListenerService;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //改为异步执行
    @Async("asyncPoll")
    public void run() throws Exception {
        System.out.println(">>>>>>>Canal服务被启动<<<<<<<<<<");
        CanalConnector canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress("localhost", 11111), "example", "", "");
        while (true) {
            //2.获取连接
            canalConnector.connect();
            //3.指定要监控的数据库
            canalConnector.subscribe("db.xiaomi.*");
            //4.获取 Message
            Message message = canalConnector.get(100);
            List<CanalEntry.Entry> entries = message.getEntries();
            if (entries.size() <= 0) {
            //    System.out.println("没有数据，休息一会");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                for (CanalEntry.Entry entry : entries) {
                    // 获取表名
                    String tableName = entry.getHeader().getTableName();
                    //  Entry 类型
                    CanalEntry.EntryType entryType = entry.getEntryType();
                    //  判断 entryType 是否为 ROWDATA
                    if (CanalEntry.EntryType.ROWDATA.equals(entryType)) {
                        //  序列化数据
                        ByteString storeValue = entry.getStoreValue();
                        //  反序列化
                        CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(storeValue);
                        // 获取事件类型
                        CanalEntry.EventType eventType = rowChange.getEventType();
                        // 获取具体的数据
                        List<CanalEntry.RowData> rowDatasList = rowChange.getRowDatasList();
                        // 遍历并打印数据
                        for (CanalEntry.RowData rowData : rowDatasList) {
                            List<CanalEntry.Column> beforeColumnsList = rowData.getBeforeColumnsList();
                            Map<String, Object> bMap = new HashMap<>();  //更新前数据
                            for (CanalEntry.Column column : beforeColumnsList) {
                                bMap.put(column.getName(), column.getValue());
                            }
                            Map<String, Object> afMap = new HashMap<>(); //更新后数据
                            List<CanalEntry.Column> afterColumnsList = rowData.getAfterColumnsList();
                            for (CanalEntry.Column column : afterColumnsList) {
                                afMap.put(column.getName(), column.getValue());
                            }
                            System.out.println("表名:" + tableName + ",操作类型:" + eventType);
                            System.out.println("改前:" + bMap);
                            System.out.println("改后:" + afMap);

                            //订单表中gno是商品的id
                            //在此处向MQ中发送消息？ 那么只针对订单详情表   --那么其他表也更新了怎么办？ 比如用户表--也会发送消息？----根据表名
                            //  orderiteminfo订单详情表    tb_phoneinfo商品详情表
                            //是否和操作类型有关？ --insert  update  delete(一般不会出现)  --所以不需要进行操作类型判断

                            // rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_NAME,MqConstants.ROUNTING_KEY,afMap);
                            //当这两个表发生变动时进行发送消息orderiteminfo订单详情表    tb_phoneinfo商品详情表
                            if (tableName.equals("tb_phoneinfo")){//这两个表有关的是商品的信息 此方法为监听这两个表变动将发送MQ
                                sendMessageToMq(afMap);
                            }
                        }
                    }
                }
            }
        }
    }

    /**   tb_phoneinfo商品详情表  商品id---id
     * 在下订后会插入订单表并且更新商品详情表中的数据 那么只需要监听tb_phoneinfo表即可
     * 向MQ发送消息
     */
    public void sendMessageToMq(Map<String,Object> msg){
        rabbitTemplate.convertAndSend(MqConstants.EXCHANGE_NAME,MqConstants.ROUNTING_KEY,msg);
    }

}
