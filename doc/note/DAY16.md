# 使用Redis缓存数据

把关系型数据库中的数据存储到Redis中，并且，后续需要查询数据时，将优先从Redis中查询（如果Redis中没有，则从关系型数据库中查询，或，如果Redis中没有，直接返回没有数据的结果），这种做法就叫“缓存”数据。

当使用Redis缓存数据后，将存在**数据一致性**的问题！其典型表现就是：如果关系型数据库中的数据发生了变化，但是，Redis中的数据没有及时一并调整，就会导致Redis中的数据与关系型数据库中的数据并不一致！

对于数据一致性问题，首先，要学会区分，你需要的到底是实时一致性，还是最终一致性。

并且，虽然Redis适合缓存大量的数据，但是，如果某些数据的访问频率非常低，其实，也没有必要缓存到Redis中（需要查询时，直接从关系型数据库中查询即可），否则，任何数据都缓存到Redis中，则任何数据都需要处理数据一致性问题。

所以，适合使用Redis缓存的数据应该是：

- 访问频率较高，甚至很高
- 对数据一致性要求并不严格
- 数据被修改的频率非常低

关于数据一致问题的解决方案大致有：

- 实时同步：修改关系型数据库中的数据时，也一并修改Redis中缓存的数据
- 手动同步：修改关系型数据库中的数据时，不会修改Redis中缓存的数据，仅当管理人员手动操作后，才会将关系型数据库的数据同步到Redis中
- 定时同步：修改关系型数据库中的数据时，不会修改Redis中缓存的数据，但每间隔一段时间，或到了某个特定的时间点，就会自动将关系型数据库的数据同步到Redis中

# 使用Redis缓存资讯的类别列表

首先，使用公共的接口文件定义Redis中的数据的Key值，例如，在`tmall-common`项目中创建此接口：

```java
public interface ContentCacheConsts {

    String KEY_CATEGORY_LIST = "content:category:list";

}
```

需要在`repository`层实现读写Redis，则先在项目的根包下创建`dao.cache.ICategoryCacheRepository`接口，此接口应该继承自以上定义Key值的接口，则此接口的实现类可以直接使用以上Key值，并在接口中声明抽象方法：

```java
public interface ICategoryCacheRepository extends ContentCacheConsts {

    void saveList(List<CategoryListItemVO> categoryList);

    List<CategoryListItemVO> list();

}
```

在项目的根包下创建`dao.cache.impl.CategoryCacheRepositoryImpl`类，实现以上接口，并重写接口中定义的抽象方法：

```java
@Repository
public class CategoryCacheRepositoryImpl implements ICategoryCacheRepository {

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public void saveList(List<CategoryListItemVO> categoryList) {
        ListOperations<String, Serializable> opsForList = redisTemplate.opsForList();
        for (CategoryListItemVO category : categoryList) {
            opsForList.rightPush(KEY_CATEGORY_LIST, category);
        }
    }

    @Override
    public List<CategoryListItemVO> list() {
        long start = 0;
        long end = -1;
        ListOperations<String, Serializable> opsForList = redisTemplate.opsForList();
        List range = opsForList.range(KEY_CATEGORY_LIST, start, end);
        return range;
    }

}
```

然后，需要在`service`层调用读写Redis的`repository`。

```java
@Override
public PageData<CategoryListItemVO> list(Integer pageNum) {
    log.debug("开始处理【查询类别列表】的业务，页码：{}", pageNum);
    // return categoryRepository.list(pageNum, defaultQueryPageSize);
    List<CategoryListItemVO> list = categoryCacheRepository.list();
    PageData<CategoryListItemVO> pageData = new PageData<>();
    pageData.setList(list);
    pageData.setMaxPage(1);
    pageData.setPageSize(list.size());
    pageData.setTotal(list.size() + 0L);
    pageData.setPageNum(1);
    return pageData;
}
```

经过以上调整后，即可达成新的效果，Controller并不需要做任何调整。

# 使用ApplicationRunner缓存预热

在Spring Boot项目中，自定义组件类，实现`ApplicationRunner`接口，则重写的方法会在项目启动时自动执行，可以在此重写的方法中向Redis存入缓存的数据，即可实现缓存预热（启动项目时即加载缓存数据到Redis中）。

示例代码：`ICategoryService`：

```java
/**
 * 重建缓存
 */
void rebuildCache();
```

示例代码：`CategoryServiceImpl`：

```java
@Override
public void rebuildCache() {
    List<CategoryListItemVO> list
            = categoryRepository.list(1, Integer.MAX_VALUE).getList();
    categoryCacheRepository.deleteList();
    categoryCacheRepository.saveList(list);
}
```

示例代码：`CategoryCachePreload`：

```java
@Slf4j
@Component
public class CategoryCachePreload implements ApplicationRunner {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        categoryService.rebuildCache();
    }

}
```

# 计划任务

在Spring Boot项目中，自定义组件类，并在组件中自定义方法，在方法上添加`@Scheduled`注解并配置计划任务的执行规则，即可使用计划任务。

注意：在Spring Boot项目中，默认并不允许执行计划任务，必须在配置类上添加`@EnableScheduling`注解以开启，才允许执行计划任务。

```java
@Slf4j
@Component
public class CategoryCacheSchedule {

    // fixedRate：执行频率，以【上一次开始执行的时间】来计算下一次的执行时间，以毫秒为单位
    // fixedDelay：执行间隔，以【上一次执行结束的时间】来计算下一次的执行时间，以毫秒为单位
    // cron：使用cron表达式来配置，cron表达式的值是一个字符串，由6~7个域组成，各域之间使用空格分隔
    // -- 在cron表达式中，各域从左至右分别表示：秒 分 时 日 月 周（星期） [年]
    // -- 各域的值可以使用通配符
    // -- 使用星号（*）表示任意值
    // -- 使用问号（?）表示不关心此域的值，仅可以用于“日”和“周”这2个域的值
    // -- 各域的值可以使用 x/y 格式的值，x表示起始值，y表示间隔周期
    // -- 例如在“分”的域位置设置为 1/5，则表示“分”的值为1时开始执行，且每间隔5分钟执行一次
    // cron表达式示例：
    // "56 34 12 13 2 ? 2023"表示：2023年2月13日12:34:56执行任务，不关心当天星期几
    // "0/30 * * * * ?"表示：每分钟的0秒时执行，且每30秒执行一次
    // 更多内容参考：
    // https://segmentfault.com/a/1190000021574315
    // https://blog.csdn.net/study_665/article/details/123506946
    @Scheduled(cron = "0 0 10 ? 8 MON")
    public void rebuildCache() {
        log.debug("CategoryCacheSchedule.rebuildCache()");
    }

}
```

# 关于单点登录的补充

此前，已经基本实现了单点登录的效果，当验证用户的登录请求通过时，会将用户的身份信息（包括：ID、用户名、权限列表）生成到JWT数据中，并将JWT响应到客户端，后续，客户端携带JWT发起请求，服务器端通过解析JWT即可识别客户端的身份（用户ID是多少？是否具备某些权限？）。

目前仍存在一些问题：

- 将权限列表存入到JWT中，会导致JWT数据特别长，并且，可能泄露某些敏感数据，所以，不应该将权限列表存入到JWT中
- 如果盗用他人的JWT，则可以伪造为他人的身份
- 无法真正的登出（退出登录）
- 如果管理员把账号禁用了，此前成功登录且未过期的用户仍可以正常访问

以上问题都可以结合Redis来解决：

- 当验证登录通过后，基于用户的ID（使用用户ID作为Key中的关键数据），将权限列表存入到Redis中，后续，解析JWT得到用户的ID等信息后，再去Redis中找出对应的权限列表
- 当验证登录时，需要获取客户端的IP地址、浏览器信息甚至设备的关键信息等，当验证登录通过时，将这些数据存入到Redis中（或存入到JWT中），后续，解析JWT时，检查后续来访时的信息与此前存入到Redis中（或JWT中）的是否匹配，如果不匹配，则视为“盗用”行为
- 如果已经完成以上第1项（使用Redis存储权限列表），当尝试登出时，删除Redis中的数据即可，后续，解析JWT得到用户的ID等信息后，如果Redis中没有匹配的数据，则视为无效的JWT
- 如果已经完成以上第1项，在存储权限列表的基础上，补充存储用户的“启用状态”，当管理员禁用用户时，将此“启用状态”修改为“禁用”值，后续，解析JWT时，需要在Redis中检查“启用状态”的值

接下来，对代码的调整大致如下：

- 当验证登录通过后（`UserServiceImpl`类中的代码）：
  - 向JWT中存入的数据调整为：用户ID、用户名、IP地址、浏览器信息
  - 向Redis中存入数据（`UserStatePO`）：权限列表、启用状态
    - 用户数据的Key：`passport:user-state:{id}`
- 当解析JWT时（Filter中代码）：
  - 如果解析成功，得到用户ID后，检查Redis中是否存在此用户ID匹配的数据，如果不存在，则过滤器直接放行（不会向`SecurityContext`存入`Authentication`数据）
  - 如果解析成功，从Redis中读取启用状态，如果为“禁用”值，则提示“禁用”，并阻止运行
  - 如果解析成功，从Redis中读取权限列表，并创建为`Authentication`存入到`SecurityContext`
- 当客户端发起登出请求时（Controller和Service中的代码）：
  - 根据当前当事人ID（登录的用户ID）检查Redis中的数据，并删除Redis中此用户ID对应的数据
- 当管理员禁用某个用户时（Service中的代码）：
  - 根据被禁用的用户ID查找Redis中的数据，如果Redis中没有匹配的信息，则不额外执行操作，如果Redis中存在匹配的信息，则将启用状态改为“禁用”值

# 关于Redis中的hash类型

在Redis中的hash类型对应Java中的`Map`类型。

建议添加`hutool`依赖，以实现对象与Map的相互转换：

```xml
<hutool.version>5.8.15</hutool.version>
```

```xml
<!-- hutool：小工具集合 -->
<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>${hutool.version}</version>
</dependency>
```

读写hash数据示例：

```java
// 存入hash类型的数据
@Test
void hashPutAll() {
    Category category = new Category();
    category.setId(998L);
    category.setName("测试类别998");

    // Map<Object, Object> map = new HashMap<>();
    // map.put("id", category.getId());
    // map.put("name", category.getName());
    Map<String, Object> map = BeanUtil.beanToMap(category);

    HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
    String key = "mall:category:item:998";
    opsForHash.putAll(key, map);
}

// 读取Redis中的hash数据
@Test
void hashEntries() {
    String key = "mall:category:item:998";
    HashOperations<String, Object, Object> opsForHash = redisTemplate.opsForHash();
    Map<Object, Object> entries = opsForHash.entries(key);
    System.out.println(entries);

    // Category category = new Category();
    // category.setId(Long.valueOf(entries.get("id").toString()));
    // category.setName(entries.get("name").toString());
    Category category = BeanUtil.mapToBean(entries, Category.class, true, null);
    System.out.println(category);
}
```



























