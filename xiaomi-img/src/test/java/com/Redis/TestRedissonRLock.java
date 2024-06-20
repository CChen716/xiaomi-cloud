package com.Redis;

import com.yc.ImgApp;
import io.lettuce.core.RedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**测试Redisson分布式锁  解决分布式集群情况下并发扣减库存问题
 * @author 12547
 * @version 1.0
 * @Date 2024/3/9 16:09
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ImgApp.class)
public class TestRedissonRLock {

    @Autowired
    private RedissonClient redissonClient;

    private String userid="1";

    private final static String LOCK="SECKILL:";
    /**
     * 测试在分布式集群redis环境下解决     ---并发秒杀业务防止”超卖“现象
     */
    @Test
    public void testRLock(){
        //此处是下订业务请求进入
        //1.设置锁
        RLock rLock=redissonClient.getLock(LOCK+userid); //模拟id
        //2.尝试获取锁
        boolean isLock = rLock.tryLock();  //非阻塞式锁
      //  rLock.lock();  lock没有返回值
        //tryLock和lock的区别就是tryLock将尝试在不阻塞的情况下获取锁，而lock将阻塞直到获得锁。
        //3.锁判断是否获取成功
        if (!isLock){
            //获取锁失败，代表有其他的线程抢到了锁,正在执行秒杀业务
            //如果使用tryLock()当其他线程没有抢到锁时则执行该代码    tryLock()是非阻塞的  没抢到锁直接返回false
            System.out.println("获取锁失败  直接返回失败信息");

        }
        //获取锁成功
        try {
             //执行秒杀下单业务
            //此处可以使用lua脚本执行扣减库存也可以不使用lua脚本扣减   因为如果即使jvm线程轮巡到其他线程 但其他线程无法获取到锁 所以不会进行执行  但还是推荐使用lua脚本实现插查扣一体
            System.out.println("线程获取锁成功 执行秒杀扣减库存业务");
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            //判断当前线程是否持有锁
            //isHeldByCurrentThread()用于判断当前线程是否持有锁，而isLocked()用于判断锁是否被任何线程持有。
            //
            if(rLock.isLocked()&rLock.isHeldByCurrentThread()) {
                rLock.unlock();//释放锁
                System.out.println("当前线程锁释放");
                //TODO 日志打印？
            }
        }

    }



}
