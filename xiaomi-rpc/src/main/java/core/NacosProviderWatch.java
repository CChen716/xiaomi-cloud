package core;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.AbstractEventListener;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import com.alibaba.nacos.common.remote.client.RpcClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.NacosConfigRpc;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** Nacos服务监听事件类
 * @author cg
 * @Date 2024/12/20 16:43
 */
public class NacosProviderWatch{

    public static ConcurrentHashMap<String,Map<String,Boolean>> HealthInstanceStatus=new ConcurrentHashMap<>(); //用来存放所有的健康实例信息

  //  private  List<String> rpcServiceKeys;//RPC相关的服务端口

    private ConcurrentHashMap<String,Boolean> rpcServiceKeys=new ConcurrentHashMap<>();//RPC相关的服务端口 应该和存放健康实例一样的结构 不然如果是list 那么下线后移除后 再次上线时该服务就会被判断非rpc相关

    private ExecutorService executorService= Executors.newFixedThreadPool(1);//创建线程池 供Nacos监听回调

    private ScheduledExecutorService scheduledExecutorService=Executors.newSingleThreadScheduledExecutor();//定时获取服务状态 并更新监听状态

    private List<Integer> delayMils= CollUtil.newArrayList(5,10,30,60,120,300,600,1800,3600);//定时任务的延时数组

    private Set<String> rpcNameSet;

    private  NamingService namingService;

    //2.自定义线程池异步监听写法  参考：nacos官网文档
    //将new EventListener改为AbstractEventListener
    private  EventListener buildNacosEventListener() {
        return new AbstractEventListener() {
            @Override
            public void onEvent(Event event) {
                //事件监听逻辑
                //此处应该已经是处理的具体的对应服务逻辑 监听的是某个服务名的所有实例情况
                //那么这个事件中就要知道监听的这个服务的实例发什么什么变化触发事件
                //1.某一个实例下线  2.某一个实例上线
                if(event instanceof NamingEvent){//确保只监听服务实例变化的事件
                    NamingEvent namingEvent= (NamingEvent) event; //Nacos事件类型对象

                    List<Instance> instances = namingEvent.getInstances();
                    if (HealthInstanceStatus.isEmpty()&&instances.isEmpty()) {
                        return; // 如果没有实例则提前退出  防止Nacos初始化通知或者输出误导
                    }
                    System.out.println("检测到服务实例变化："+ namingEvent);
                    String serviceName=namingEvent.getServiceName();
                    System.out.println("serviceName："+serviceName); //服务名称
                    if (!HealthInstanceStatus.containsKey(serviceName)){
                        ConcurrentHashMap<String,Boolean> instanceMap=new ConcurrentHashMap<>();
                        HealthInstanceStatus.put(serviceName,instanceMap);
                        instances.forEach(instance -> { //遍历每个实例 将其健康状态存入map中
                            String instanceKey=instance.getIp()+":"+instance.getPort(); //ip:端口作为键名  value为是否健康
                            instanceMap.put(instanceKey,instance.isHealthy());
                            System.out.println("服务首次上线："+instanceKey);
                        });
                        return;
                    }
                    //到这里表示已经存在的服务实例发生变化 然后需要获取所有实例 因为nacos暂时无法获取指定单个实例变化情况
                    List<ServiceInfo> allServiceInstance = getAllServiceInstances();

                    int instanceTotal = allServiceInstance.stream()
                            .mapToInt(serviceInfo -> Integer.parseInt(serviceInfo.getClusters()))
                            .sum();//算出所有在线服务实例

                    System.out.println("当前在线实例数量"+instanceTotal);
                    //检验这个服务是否存在map当中 并获取健康状态映射map
                    //获取这个服务名的上一次保存的所有映射健康状态 获取所有为true的key
                    HashSet<String> oldInstanceKey = HealthInstanceStatus.entrySet().stream()
                            .filter(entry -> entry.getValue().values().stream().anyMatch(value -> value)) //返回为ture的的键
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toCollection(HashSet::new));

                    List<Instance> newInstances = namingEvent.getInstances();//该方法是获取最新的一次服务实例列表

                    //然后将最新的服务实例列表一一遍历 取出ip和端口作为键名
                    Set<String> newInstanceKeys = newInstances.stream()
                            .map(instance -> instance.getIp() + ":" + instance.getPort())
                            .collect(Collectors.toSet());


                    //为了避免连续下线 后连续上线的判断 所以需要获取所有健康的实例数量  而不是存放的所有实例
                    int oidSize=oldInstanceKey.size();
                    int newSize=newInstances.size();//获取最新的服务实例列表数量·
                    //那么就是会出现上线和下线两种情况

                    //如果下线实例
                    if (oidSize>newSize){
                        //TODO：下线通知逻辑
                        newInstanceKeys.forEach(oldInstanceKey::remove); //这里是将新获取的所有服务实例在老服务实例中一一对应移除 那么老服务实例剩下的就是下线的实例
                        //假设oldInstanceKey这个set中有 1 2 3这3个实例 然后2实例下线
                        //那么newInstanceKey获取到的就是 1 3这2个实例
                        //然后将oldInstanceKey中遍历newInstanceKey 然后移除  那么oldInstanceKey就只剩下2了
                        //那么就知道实例2是下线了的 遍历oldInstanceKey就可以知道哪些是下线的实例
                        oldInstanceKey.forEach(instanceKey-> {
                            System.out.println("检测到服务下线："+instanceKey);
                            //判断是否是rpc相关的服务 如果是则需要通知处理
                            if (rpcServiceKeys.contains(instanceKey)){
                                //如果是 则需要进行消费端配置更新处理
                                outRpcService(instanceKey);
                            }
                            //然后需要更新实例健康映射状态
                            Map<String, Boolean> stringBooleanMap = HealthInstanceStatus.get(serviceName);
                            stringBooleanMap.put(instanceKey,false);//更新为下线
                        });


                    }else {//如果某个服务上线一个新实例后会走到这 而不是上面=的情况
                        newInstanceKeys.removeAll(oldInstanceKey); //将最新获取的服务实例列表中移除已经记录的旧服务实例  那么留下来的就是新上线的实例
                        newInstanceKeys.forEach(instanceKey-> {
                            System.out.println("检测到服务上线："+instanceKey);
                            if (rpcServiceKeys.contains(instanceKey)){
                                //如果是 则需要进行消费端配置更新处理
                                onlineRpcService(instanceKey);
                            }

                            Map<String, Boolean> stringBooleanMap = HealthInstanceStatus.get(serviceName);
                            stringBooleanMap.put(instanceKey,true);//更新为上线

                        });
                    }
                }
            }


            //此处可以返回自己的自定义异步线程池  Nacos客户端会在服务发生变更的时候使用该线程池执行异步回调
            //注意这里只创建了1个线程的线程池来执行回调 如果是多个的话会产生并发安全问题。 上下线的事件只能顺序处理
            @Override
            public Executor getExecutor() {
                return executorService;
            }
        };

    }


    public List<ServiceInfo> getAllServiceInstances() {
        List<ServiceInfo> serviceInfos = new ArrayList<>();
        try {
            List<String> services = namingService.getServicesOfServer(1, Integer.MAX_VALUE).getData();
            for (String serviceName : services) {
                List<Instance> onlineInstances = namingService.selectInstances(serviceName, true);
                // 下线服务暂时不用关注
                List<Instance> offlineInstances = namingService.selectInstances(serviceName, false);
                serviceInfos.add(new ServiceInfo(serviceName, String.valueOf(onlineInstances.size())));
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return serviceInfos;
    }


    /**
     * rpc相关服务下线处理
     * @param rpcInstanceKey
     */
    private void outRpcService(String rpcInstanceKey){
        rpcServiceKeys.put(rpcInstanceKey,false);
        //TODO:更新配置  目前只更新本地的config对象和后续的缓存  而消费端是否可以更新配置文件 后续考虑服务端的配置文件更新功能来决定是否开发
        List<String> updateList = NacosConfigPull.rpcUrlMap.get(NacosConfigRpc.server);
        updateList.remove(rpcInstanceKey);
        NacosConfigPull.rpcUrlMap.put(NacosConfigRpc.server,updateList);
    }


    /**
     * rpc相关服务上线处理
     * @param rpcInstanceKey
     */
    private void onlineRpcService(String rpcInstanceKey){
        rpcServiceKeys.put(rpcInstanceKey,true);
        //TODO:更新配置
        List<String> updateList = NacosConfigPull.rpcUrlMap.get(NacosConfigRpc.server);
        updateList.add(rpcInstanceKey);
        NacosConfigPull.rpcUrlMap.put(NacosConfigRpc.server,updateList);
    }

    /**
     * 监听类启动方法
     * @throws NacosException
     */
    public  void start(String address)  {
        //正确做法：来源 nacos官方示例
        //通过Properties对象创建NamingService
        Properties properties = new Properties();
        properties.setProperty("serverAddr", address); //此处后续改为 从配置对象中获取用户nacos地址
        properties.setProperty("namespace", "public");
        //需要从工厂中创建  因为没有用使用springBoot框架 所以要手动创建properties对象以及NamingService对象

        //初始化namingService
        try {
            namingService = NamingFactory.createNamingService(properties);
            List<String> nacosRpcList = NacosConfigPull.rpcUrlMap.get(NacosConfigRpc.server);//取出消费端拉取下来的配置端口信息

            //后续此处key改为截取ip保存
            for (String rpcInstance : nacosRpcList) {
                String rpcIp = rpcInstance.split(":")[0];
                rpcServiceKeys.put(rpcIp,true);//rpc相关服务端口
            }

            //1.单个监听普通写法：
            // namingService.subscribe("xiaomi-cart",buildNacosEventListener()); //经过测试 只会监听对应的服务名 所以要确定服务名称
            //可以根据ip和端口号获取到对应的服务名 然后只需要监听指定的这几个服务就可以了
            //服务端注册的配置就是 服务的ip:端口  那么和配置相符合的就是我们需要监听的对象

            //此处需要根据rpc接口提供的ip和端口 监听对应的几个服务名
             rpcNameSet = transServerName(); //这一步就是返回的所有服务实例中的 rpc相关的服务实例

            for (String rpcName : rpcNameSet) {//此处如果有多个服务 那么就会创建多个监听器 每个监听器监听自己独立的服务
                namingService.subscribe(rpcName,buildNacosEventListener());
                System.out.println("正在监听服务:"+rpcName);//只需要监听对应的服务
                initHealthInstanceStatusMap(rpcName);//初始化map
            }
            //需要将当前的服务健康map初始化
            scheduledCheckService(); //TODO:定时检查服务实例 并补充注册事件监听器

        } catch (NacosException e) {
            e.printStackTrace();
        }


    }

    /**
     * 监听对应的服务
     * @param
     * @return
     */
    private Set<String> transServerName() throws NacosException {
        //该功能也是属于监听功能初始化中的一个步骤 所以此时拉取下来的NacosConfig中的服务ip列表，一定是此时已经注册在nacos上面的服务 所以可以根据配置文件上的ip端口来决定监听哪些服务
        List<String> serverNameList = namingService.getServicesOfServer(1, Integer.MAX_VALUE).getData(); //获取服务列表名
        HashSet<String> rpcServerName=new HashSet<>();
        for (String serverName : serverNameList) { //遍历服务名
            List<Instance> allInstances = namingService.getAllInstances(serverName); //根据每个服务名找到对应的实例数量
            for (Instance instance : allInstances) {//遍历每个服务的实例
             String key=instance.getIp(); //ip 和端口   TODO；后续考虑到和Spring启动服务器端口冲突问题 改为只要ip符合就监听 是否是rpc端口就不考虑
             if (rpcServiceKeys.containsKey(key)){
                 rpcServerName.add(serverName); //将匹配的ip端口的服务名添加到set集合中
                 break;
              }
            }
        }
        return rpcServerName;
    }

    /**
     * 定时获取服务列表并更新服务的订阅  该方法是应对nacos监听功能无法订阅所有服务 只能手动获取实例后进行订阅 也就是需要被监听的服务实例必须先运行起来才可以监听
     * 所以该方法是解决了 强制服务实例启动的顺序
     *
     */
    public void scheduledCheckService(){

            Runnable task=new Runnable() {
                @Override
                public void run() {
                    try {
                        Set<String> set = transServerName(); //获取当前的和rpc相关的服务列表
                        //然后需要和初始化启动的时候中的列表进行对比 如果出现新的话那么就需要订阅 并更新订阅服务set集合
                        for (String server : set) {
                            if (!rpcNameSet.contains(server)){ //如果是包含的则代表已经监听这个服务了
                                namingService.subscribe(server,buildNacosEventListener()); //如果不包含则代表监听到新的 rpc服务上线了 进行监听事件注册
                                initHealthInstanceStatusMap(server); //同样将该服务实例的健康状态补充到map中
                                System.out.println("正在监听服务:"+server);//只需要监听对应的服务
                                rpcNameSet.add(server);//将新的rpc相关服务名添加到set集合中
                            }
                        }
                        //如果还有下一个延时 则进行递归调度
                        if (!delayMils.isEmpty()){
                            scheduledExecutorService.schedule(this,delayMils.remove(0),TimeUnit.SECONDS); //这里this是指向runnable对象也就是自身 这里是匿名内部类
                        }else {
                            //如果执行完毕了就关闭
                            executorService.shutdown();
                        }

                    } catch (NacosException e) {
                        e.printStackTrace();
                    }
                }
            };

            //初始化执行调度任务
            if (!delayMils.isEmpty()){
                scheduledExecutorService.schedule(task,delayMils.remove(0),TimeUnit.SECONDS); //执行定时任务
            }else {
                executorService.shutdown();
            }

    }


    /**
     * 初始化健康实例map
     * @param serverName
     */
    private void initHealthInstanceStatusMap(String serverName){ //根据监听的服务名 获取实例 添加到map中

        try {
            List<Instance> serviceAllInstances = namingService.getAllInstances(serverName);
            for (Instance instance : serviceAllInstances) {
                String ip = instance.getIp(); //ip
                Integer port = instance.getPort();//端口
                Map<String, Boolean> serverMap = HealthInstanceStatus.getOrDefault(serverName, new ConcurrentHashMap<>());
                String serverKey=ip+":"+port;
                serverMap.put(serverKey,true); //能获取到的实例 一定是在线的 所以为true
                HealthInstanceStatus.put(serverName,serverMap);
            }
        } catch (NacosException e) {
            e.printStackTrace();
        }

    }





//    public static void main(String[] args) throws InterruptedException, NacosException {
//        RpcConfigApplication.init();
//        NacosConfigPull.pull();
//        NacosProviderWatch nacosProviderWatch = new NacosProviderWatch();
//        nacosProviderWatch.start(); //先初始化
//        //对于这种初始化的顺序 后续可以通过模板方法进行优化
//        Thread.sleep(Long.MAX_VALUE);
//    }





}
