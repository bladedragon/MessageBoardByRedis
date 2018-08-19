## 进阶Redis+留言板实例##



#### redis回顾####

***

**1.redis概念**

Redis是一个开源的使用ANSI C语言编写、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库

**2.redis的优点**

- *异常快* 

  Redis非常快，每秒可执行大约`11000次的设置(`SET`)操作，每秒大约可执行`81000`次的读取/获取(`GET`)操作。

- *支持丰富的数据类型* 

  Redis支持开发人员常用的大多数数据类型，例如列表，集合，排序集和散列等等。这使得Redis很容易被用来解决各种问题，因为我们知道哪些问题可以更好使用地哪些数据类型来处理解决。

- *操作具有原子性* 

  所有Redis操作都是原子操作，这确保如果两个客户端并发访问，Redis服务器能接收更新的值。

- *多实用工具* 

   Redis是一个多实用工具，可用于多种用例，如：缓存，消息队列(Redis本地支持发布/订阅)，应用程序中的任何短期数据，例如，web应用程序中的会话，网页命中计数等

  

**3.redis的应用**

- *热数据的缓存*

  热点数据（经常会被查询，但是不经常被修改或者删除的数据），首选是使用redis缓存 

- *计数器*

  诸如统计点击数等应用。由于单线程，可以避免并发问题 

- *位操作（大数据处理）*

  redis内构建一个足够长的数组，每个数组元素只能是0和1两个值，然后这个数组的下标index用来表示用户id（必须是数字），那么很显然，这个几亿长的大数组就能通过下标和元素值（0和1）来构建一个记忆系统，用到的命令是：`setbit`、`getbit`、`bitcount` 

- *分布式锁*\*

  验证前端的重复请求（可以自由扩展类似情况），可以通过redis进行过滤：每次请求将request Ip、参数、接口等hash作为key存储redis（幂等性请求），设置多长时间有效期，然后下次请求过来的时候先在redis中检索有没有这个key，进而验证是不是一定时间内过来的重复提交

  秒杀系统，基于redis是单线程特征，防止出现数据库“爆破”

- *排行榜*

  

**4.redis数据结构**

![五种数据类型](C:\Users\11566\Desktop\redis\数据结构.png)



* [**dos环境下使用命令：**](https://www.cnblogs.com/kevinws/p/6281395.html)
* [**java中常用命令：**]()

1. Stirng

   - `set void set(K key, V value);`

   - `set void set(K key, V value, long timeout, TimeUnit unit);`

   - `get V get(Object key)`

     ```java
     redisTemplate.opsForValue().set("name","tom");  //使用
     redisTemplate.opsForValue().get("name")  //输出结果为tom
     /*---------------------------------------------------------------------*/
     redisTemplate.opsForValue().set("name","tom",10, TimeUnit.SECONDS); //使用
     redisTemplate.opsForValue().get("name") //返回结果。由于设置的是10秒失效，十秒之内查询有结果，十秒之后返回为null
     
     ```

   - `increment Long increment(K key, long delta);`
     支持整数或浮点数

     ```java
     template.opsForValue().increment("increlong",1);//使用
             System.out.println("***************"+template.opsForValue().get("increlong"));
     
     ***************1                         //输出
     ```

     

   - append Integer append(K key, String value);
     如果key已经存在并且是一个字符串，则该命令将该值追加到字符串的末尾。如果键不存在，则它被创建并设置为空字符串，因此APPEND在这种特殊情况下将类似于SET。

     ```Java
     //使用
     template.opsForValue().append("appendTest","Hello");
             System.out.println(template.opsForValue().get("appendTest"));
             template.opsForValue().append("appendTest","world");
             System.out.println(template.opsForValue().get("appendTest"));
     //输出
     Hello
             Helloworld
     
     ```

    

2. List

   - `void set(K key, long index, V value);void set(K key, long index, V value);void set(K key, long index, V value);void set(K key, long index, V value);`
     在列表中index的位置设置value值

   - `List<V> range(K key, long start, long end);`
     返回存储在键中的列表的指定元素。偏移开始和停止是基于零的索引，其中0是列表的第一个元素（列表的头部），1是下一个元素

     ```java
     //使用
     System.out.println(template.opsForList().range("list",0,-1));
     ///输出
     [c#, c++, python, java, c#, c#]
     ```

     

   - `void trim(K key, long start, long end);`
     修剪现有列表，使其只包含指定的指定范围的元素，起始和停止都是基于0的索引

     ```java
     //使用
     System.out.println(template.opsForList().range("list",0,-1));
     template.opsForList().trim("list",1,-1);//裁剪第一个元素
     System.out.println(template.opsForList().range("list",0,-1));
     //输出
     [c#, c++, python, java, c#, c#]
     [c++, python, java, c#, c#]
     ```

     

   - `Long leftPush(K key, V value);`
     将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）

   - `Long leftPushAll(K key, V... values);`
     批量把一个数组插入到列表中

   - `V leftPop(K key);`
     弹出最左边的元素，弹出之后该值在列表中将不复存在

     ```java
     //使用
     template.opsForList().leftPush("list","java");
             template.opsForList().leftPush("list","python");
             template.opsForList().leftPush("list","c++");
     //返回的结果为推送操作后的列表的长度
     1
     2
     3
     
     ```

     

   - `Long remove(K key, long count, Object value);Long remove(K key, long count, Object value);`
     从存储在键中的列表中删除等于值的元素的第一个计数事件。
     计数参数以下列方式影响操作：
     count> 0：删除等于从头到尾移动的值的元素。
     count <0：删除等于从尾到头移动的值的元素。
     count = 0：删除等于value的所有元素。

     ```java
     System.out.println(template.opsForList().range("listRight",0,-1));
             template.opsForList().remove("listRight",1,"setValue");//将删除列表中存储的列表中第一次次出现的“setValue”。
             System.out.println(template.opsForList().range("listRight",0,-1));
     //输出
     [java, setValue, oc, c++]
     [java, oc, c++]
     
     ```

    

3. Hash

   - `Long delete(H key, Object... hashKeys);`
     删除给定的哈希hashKeys

   - `Boolean hasKey(H key, Object hashKey);`
     确定哈希hashKey是否存在

   - `HV get(H key, Object hashKey);`
     从键中的哈希获取给定hashKey的值

   - `Long increment(H key, HK hashKey, long delta);`
     通过给定的delta增加散列hashKey的值（整型或浮点数）

   - `Set\<HK\>keys(H key);`
     获取key所对应的散列表的key

   - `Map<HK, HV> entries(H key);`
     获取整个哈希存储根据密钥

   - `Cursor<Map.Entry<HK, HV>> scan(H key, ScanOptions options);`
     使用Cursor在key的hash中迭代，相当于迭代器。

     ```java
     Cursor<Map.Entry<Object, Object>> curosr = template.opsForHash().scan("redisHash", ScanOptions.ScanOptions.NONE);
             while(curosr.hasNext()){
                 Map.Entry<Object, Object> entry = curosr.next();
                 System.out.println(entry.getKey()+":"+entry.getValue());
             }
     //输出
     age:28.1
     class:6
     kkk:kkk
     
     ```

     

4. Set

   - `Long add(K key, V... values);`
     无序集合中添加元素，返回添加个数
     也可以直接在add里面添加多个值 如：template.opsForSet().add("setTest","aaa","bbb")

   - `Long remove(K key, Object... values);`
     移除集合中一个或多个成员

   - `V pop(K key);`
     移除并返回集合中的一个随机元素

   - `Boolean isMember(K key, Object o);`
     判断 member 元素是否是集合 key 的成员

   - `Set<V> intersect(K key, K otherKey);`
     key对应的无序集合与otherKey对应的无序集合求交集

     ```java
     System.out.println(template.opsForSet().members("setTest"));
             System.out.println(template.opsForSet().members("setTest2"));
             System.out.println(template.opsForSet().members("setTest3"));
             List<String> strlist = new ArrayList<String>();
             strlist.add("setTest2");
             strlist.add("setTest3");
             System.out.println(template.opsForSet().intersect("setTest",strlist));
     //输出
     [aaa, ccc]
     [aaa]
     [ccc, aaa]
     
     ```

     

   - `Long intersectAndStore(K key, K otherKey, K destKey);`
     key无序集合与otherkey无序集合的交集存储到destKey无序集合中

     ```java
     System.out.println("setTest:" + template.opsForSet().members("setTest"));
     System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
     System.out.println(template.opsForSet().intersectAndStore("setTest","setTest2","destKey1"));
     System.out.println(template.opsForSet().members("destKey1"));
     //输出
     setTest:[ddd, bbb, aaa, ccc]
     setTest2:[ccc, aaa]
     2
     [aaa, ccc]
     
     ```

     

   - `Set<V> union(K key, K otherKey);`
     key无序集合与otherKey无序集合的并集

     ```java
     System.out.println("setTest:" + template.opsForSet().members("setTest"));
             System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
             System.out.println(template.opsForSet().union("setTest","setTest2"));
     //输出
     setTest:[ddd, bbb, aaa, ccc]
     setTest2:[ccc, aaa]
     [ccc, aaa, ddd, bbb]
     j
     ```

     

   - `Set<V> difference(K key, K otherKey);`
     key无序集合与otherKey无序集合的差集

     ```java
     System.out.println("setTest:" + template.opsForSet().members("setTest"));
             System.out.println("setTest2:" + template.opsForSet().members("setTest2"));
             System.out.println(template.opsForSet().difference("setTest","setTest2"));
     //输出
     setTest:[ddd, bbb, aaa, ccc]
     setTest2:[ccc, aaa]
     [bbb, ddd]
     
     ```

     

   - `Set<V> members(K key);`
     返回集合中的所有成员

   - `Cursor<V> scan(K key, ScanOptions options);`
     遍历set

5. ZSet

   - `Boolean add(K key, V value, double score);`
     新增一个有序集合，存在的话为false，不存在的话为true

   - `Long remove(K key, Object... values);`
     从有序集合中移除一个或者多个元素

   - `Double incrementScore(K key, V value, double delta);`
     增加元素的score值，并返回增加后的值

   - `Long rank(K key, Object o);`
     返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列

     ```java
     //输入
     System.out.println(template.opsForZSet().range("zset1",0,-1));
             System.out.println(template.opsForZSet().rank("zset1","zset-2"));
     //输出
     [zset-2, zset-1, zset-3, zset-4, zset-5]
     0   //表明排名第一
     
     ```

     

   - `Long reverseRank(K key, Object o);`
     返回有序集中指定成员的排名，其中有序集成员按分数值递减(从大到小)顺序排列

   - ```java
     //输入
     System.out.println(template.opsForZSet().range("zset1",0,-1));
             System.out.println(template.opsForZSet().reverseRank("zset1","zset-2"));
     //输出
     [zset-2, zset-1, zset-3, zset-4, zset-5]
     4 //递减之后排到第五位去了
     
     ```

   - `Set<V> range(K key, long start, long end);`
     通过索引区间返回有序集合成指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列

   - `Set<V> rangeByScore(K key, double min, double max);`
     通过分数返回有序集合指定区间内的成员，其中有序集成员按分数值递增(从小到大)顺序排列

     ```java
     //输入
     Set<ZSetOperations.TypedTuple<Object>> tuples = template.opsForZSet().rangeWithScores("zset1",0,-1);
             Iterator<ZSetOperations.TypedTuple<Object>> iterator = tuples.iterator();
             while (iterator.hasNext())
             {
                 ZSetOperations.TypedTuple<Object> typedTuple = iterator.next();
                 System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
             }
     //输出
     value:zset-2score:1.2
     value:zset-1score:2.2
     value:zset-3score:2.3
     value:zset-4score:6.6
     value:zset-5score:9.6
     
     ```

   - `Long zCard(K key);`
     获取有序集合的成员数



##留言板实例##



####1.简单介绍思路####

springboot+redis+thymeleaf



*redis配置*

```java
#redis配置
  redis:
#  数据库索引
    database: 0
#    服务器地址
    host: 127.0.0.1
#    服务器连接端口
    port: 6379
#    链接密码
    password:
#    链接池
    pool:
#    最大连接数（负值表示没有限制）
      max-active: 8
#      最大阻塞等待时间（负值表示没有限制）
      max-wait: 1
#      最大空闲链接
      max-idle: 8
#      最小空闲链接
      min-idle: 0
#    链接超时时间（毫秒）
    timeout: 0
```



坑：

* bean类一定要继承serializable接口

* 当跳转到一个含有form表单的页面的时候，必须要new一个同名实例给前端页面来接收表单中的参数

* 跳转时post请求一定要重定向，否则无法正常刷新

* 百度大法好

  

####2.redis序列化####

存取对象到redis的方法？

1. 对象转json格式

   ```java
   //PS:因为留言板里没采用这种方式，所以就在网上贴了段代码
   public class RedisOps {
       public static void setJsonString(String key,Object object){
           Jedis jedis = RedisConnection.getJedis();
           jedis.set(key, JSON.toJSONString(object));
           jedis.close();
       }
       public static Object getJsonObject(String key,Class clazz){
           Jedis jedis = RedisConnection.getJedis();
           String value = jedis.get(key);
           jedis.close();
           return JSON.parseObject(value,clazz);
       }
   }
   ```

   

2. 利用HashMap存储对象

   ```java
   //用Map存储对象属性，再将Map以键值对形式压入HashMap 
   publilc class RedisServiceImp{
     
     public void submitMsg(String username, String msg){
     String word = "word:" + messageId;
           HashMap<String,String> messageData = new HashMap<String,String>();
           messageData.put("username", username);
           messageData.put("msg", msg);
           messageData.put("date", date);
   
           messageRedisTemplate.opsForHash().putAll(word, messageData);
         }
     }
   ```

   

3. 序列化对象

   ```java
   //采用了json的构造器使序列化后的数据以json形式存储在redis里
   @Configuration
   public class RedisConfig {  //构造属于自己的序列化器
       @Bean
       public RedisTemplate<Object, Message> messageRedisTemplate(
               RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
           RedisTemplate<Object, Message> template = new RedisTemplate<Object, Message>();
           template.setConnectionFactory(redisConnectionFactory);
           //使用json的序列化器
           Jackson2JsonRedisSerializer ser = new Jackson2JsonRedisSerializer<Message>(Message.class);
           template.setDefaultSerializer(ser);                 //相当于key的序列化类型和value的序列化类型
           return template;
       }
   
   
   }
   ```

   显示在Redis Destop Manager的情况：

   ![在RedisManager里的显示](C:\Users\11566\Desktop\redis\QQ图片20180818233348.png)



#### 3.redis缓存####

> 通过redis缓存数据的目的不是加快查询的速度，而是减少数据库的负担。　 



PS：由于redis用来缓存数据库的数据，留言板里直接使用redis存储数据，所以就不予实例展示，以下代码均来源网络

```java
//为了让SpringBoot内置的缓存框架使用我们的Redis作为新的缓存，我们来添加一个RedisConfiguration的配置类 	
@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurerSupport {

    /**
     * 自定义生成key的规则
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                //格式化缓存key字符串
                StringBuilder sb = new StringBuilder();
                //追加类名
                sb.append(o.getClass().getName());
                //追加方法名
                sb.append(method.getName());
                //遍历参数并且追加
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println("调用Redis缓存Key : " + sb.toString());
                return sb.toString();
            }
        };
    }
    /**
     * 采用RedisCacheManager作为缓存管理器
     * @param redisTemplate
     * @return
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        return new RedisCacheManager(redisTemplate);
    }
}

//用@EnableCaching注解来开启我们的项目支持缓存，我们在配置类内添加了方法cacheManager()，方法的返回值则是使用了我们的Redis缓存的管理器，SpringBoot项目启动时就会去找自定义配置的CacheManager对象并且自动应用到项目中。 	

```

配置好缓存配置类后，还需要在service类上加`@CacheConfig(cacheNames= "") `,在service类的方法上加`@Cacheable `注解



`@CacheConfig`：该注解是用来开启声明的类参与缓存,如果方法内的`@Cacheable`注解没有添加key值，那么会自动使用cahceNames配置参数并且追加方法名。 

![service层的注解](..\redis\QQ图片20180819001733.png)

`@Cacheable`：配置方法的缓存参数，可自定义缓存的key以及value。 

![service层方法的注解](..\redis\QQ图片20180819001747.png)



> 测试结果:当使用mysql数据库存储数据时，前台多次刷新获取数据列表，后台数据库操作只有一次,说明之后获取的都是redis存储的缓存数据。

我们甚至可以自定义key规则

```java
    /**
     * 自定义生成key的规则
     * @return
     */
    @Override
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                //格式化缓存key字符串
                StringBuilder sb = new StringBuilder();
                //追加类名
                sb.append(o.getClass().getName());
                //追加方法名
                sb.append(method.getName());
                //遍历参数并且追加
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println("调用Redis缓存Key : " + sb.toString());
                return sb.toString();
            }
        };
    }
```

输出

PS：图片来自网络！

![自定义规则](..\redis\4461954-be863ece303654bb.png)

#### 4.SpringBoot的 web渲染方式####

Spring Boot提供了默认配置的模板引擎主要有以下几种：

- Thymeleaf
- FreeMarker
- Velocity
- Groovy
- Mustache

Spring Boot建议使用这些模板引擎，避免使用JSP，若一定要使用JSP将无法实现Spring Boot的多种特性 

**这里以Thymeleaf为例**

1. 概念

   - Thymeleaf在有网络和无网络的环境下皆可运行，即它可以让美工在浏览器查看页面的静态效果，也可以让程序员在服务器查看带数据的动态页面效果。这是由于它支持 html 原型，然后在 html 标签里增加额外的属性来达到模板+数据的展示方式。浏览器解释 html 时会忽略未定义的标签属性，所以thymeleaf的模板可以静态地运行；当有数据返回到页面时，Thymeleaf 标签会动态地替换掉静态内容，使页面动态显示。
   - Thymeleaf开箱即用的特性。它提供标准和spring标准两种方言，可以直接套用模板实现JSTL、OGNL表达式效果，避免每天套模板、改jstl、改标签的困扰。同时开发人员也可以扩展和创建自定义的方言。
   - Thymeleaf提供spring标准方言和一个与SpringMVC完美集成的可选模块，可以快速的实现表单绑定、属性编辑器、国际化等功能。

   

2. 配置

   * 导入依赖

     ```
     <dependency>
     <groupId>org.springframework.boot</groupId> 
     <artifactId>spring-boot-starter-thymeleaf</artifactId>
     </dependency>
     ```

     

   * 配置文件

     | 参数                                            | 介绍                                                       |
     | ----------------------------------------------- | ---------------------------------------------------------- |
     | spring.thymeleaf.cache = true                   | 启用模板缓存（开发时建议关闭）                             |
     | spring.thymeleaf.check-template = true          | 检查模板是否存在，然后再呈现                               |
     | spring.thymeleaf.check-template-location = true | 检查模板位置是否存在                                       |
     | spring.thymeleaf.content-type = text/html       | Content-Type值                                             |
     | spring.thymeleaf.enabled = true                 | 启用MVC Thymeleaf视图分辨率                                |
     | spring.thymeleaf.encoding = UTF-8               | 模板编码                                                   |
     | spring.thymeleaf.excluded-view-names =          | 应该从解决方案中排除的视图名称的逗号分隔列表               |
     | spring.thymeleaf.mode = HTML5                   | 应用于模板的模板模式。另请参见StandardTemplateModeHandlers |
     | spring.thymeleaf.prefix = classpath:/templates/ | 在构建URL时预先查看名称的前缀                              |
     | spring.thymeleaf.suffix = .html                 | 构建URL时附加查看名称的后缀                                |
     | spring.thymeleaf.template-resolver-order =      | 链中模板解析器的顺序                                       |
     | spring.thymeleaf.view-names =                   | 可以解析的视图名称的逗号分隔列表                           |

3. 语法

   * 前端html页面标签中引入如下：

   ```
   <html xmlns:th="http://www.thymeleaf.org">
   ```

   *  简单表达式 

     * 可用值表达式(后台设置): ${…}
     * 所有可用值表达式: *{…}

     > 比如*{name} 从可用值中查找name，如果有上下文，比如上层是object，则查object中的name属性。

     * 消息表达式: #{…} 

     > 国际化时使用，也可以使用内置的对象，比如date格式化数据

     * 链接表达式: @{…} 

     > 用来配合link src href使用的语法

     * 片段表达式: ~{…} 

     > 用来引入公共部分代码片段，并进行传值操作使用的语法。

     

     

     ***推荐教程***

     [thymeleaf快速入门教程](https://blog.csdn.net/u014042066/article/details/75614906)

 

