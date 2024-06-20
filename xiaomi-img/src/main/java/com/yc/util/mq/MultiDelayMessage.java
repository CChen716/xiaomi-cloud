package com.yc.util.mq;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.List;

/**延迟消息实体类
 * @author 12547
 * @version 1.0
 * @Date 2024/3/17 22:02
 */
@Data
public class MultiDelayMessage<T> {

    //消息体
    private T data;
    //延时消息数组
    private List<Long> delayMils;

    public MultiDelayMessage(T data,List<Long> delayMils){
        this.data=data;
        this.delayMils=delayMils;
    }


    public static <T> MultiDelayMessage<T> of(T data, Long ...delayMils){
        return new MultiDelayMessage<>(data, CollUtil.newArrayList(delayMils));
    }

    //获取延迟队列的下一个延迟时间并返回
    public Long removeNextDelay(){
        return delayMils.remove(0);   //从延迟时间列表中移除并返回下一个延迟时间
    }

}
