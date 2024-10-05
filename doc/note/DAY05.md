# MyBatis Plus（续）

如果需要自定义查询，应该：

- 创建所需的VO类
- 在Mapper接口中声明抽象方法
- 在XML文件中配置抽象方法映射的SQL语句
- 在配置文件中指定XML文件的位置（一次性配置）

则先在项目的根包（创建项目时就有的包，也可以理解为启动类所在的包）创建`pojo.vo.CategoryStandardVO`类：

```java
@Data
public class CategoryStandardVO implements Serializable {

    /**
     * 数据ID
     */
    private Long id;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 父级类别ID，如果无父级，则为0
     */
    private Long parentId;

    /**
     * 深度，最顶级类别的深度为1，次级为2，以此类推
     */
    private Integer depth;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    private String keywords;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 图标图片的URL
     */
    private String icon;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否为父级（是否包含子级），1=是父级，0=不是父级
     */
    private Integer isParent;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;

    /**
     * 数据创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 数据最后修改时间
     */
    private LocalDateTime gmtModified;

}
```

提示：以上VO类中包含实体类中所有的属性，可能存在多余的属性，可以在后续细化项目功能时再删除其中多余的属性。

在`CategoryMapper`中添加抽象方法：

```java
/**
 * 根据ID查询类别
 * @param id 类别ID
 * @return 匹配的类别，如果没有匹配的数据，则返回null
 */
CategoryStandardVO getStandardById(Long id);
```

在`src/main/resources`下创建`mapper`文件夹，并在此文件夹中使用XML配置SQL语句：

```mysql
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.tedu.tmall.admin.mall.dao.persist.mapper.CategoryMapper">

    <!-- CategoryStandardVO getStandardById(Long id); -->
    <select id="getStandardById" resultType="">
        SELECT
            id, name, parent_id, depth, keywords,
            sort, icon, enable, is_parent, is_display,
            gmt_create, gmt_modified
        FROM
            mall_category
        WHERE
            id=#{id}
    </select>

</mapper>
```

注意：如果XML文件在`src/main/resources`下名为`mapper`的文件夹下，并不需要在配置文件中指定XML文件的位置，如果使用的文件夹的名称不是`mapper`（例如使用`mappers`），则必须在配置文件中使用`mybatis-plus.mapper-locations`属性进行配置，例如：

```yaml
mybatis-plus:
  mapper-locations: classpath:mappers/*.xml
```

# 使用MyBatis Plus处理数据的创建时间和最后修改时间

MyBatis Plus提供了`MetaObjectHandler`接口，允许自定义组件类实现此接口，并重写方法，以决定自动处理数据的创建时间和最后修改时间！

可以自行创建此组件类：

```java
package cn.tedu.tmall.admin.mall.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 基于MyBatis Plus的自动填充时间的处理器类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Component
public class TimeMetaObjectHandler implements MetaObjectHandler {

    /**
     * 数据创建时间的属性名
     */
    public static final String FIELD_CREATE_TIME = "gmtCreate";
    /**
     * 数据最后修改时间的属性名
     */
    public static final String FIELD_UPDATE_TIME = "gmtModified";

    public TimeMetaObjectHandler() {
        log.info("创建MyBatis Plus的自动填充数据的处理器对象：TimeMetaObjectHandler");
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName(FIELD_CREATE_TIME, now, metaObject);
        this.setFieldValByName(FIELD_UPDATE_TIME, now, metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        this.setFieldValByName(FIELD_UPDATE_TIME, now, metaObject);
    }

}
```

并且，需要在实体类中需要自动填充时间的属性上添加注解进行配置，例如：

```java
/**
 * 数据创建时间
 */
@TableField(fill = FieldFill.INSERT)
private LocalDateTime gmtCreate;

/**
 * 数据最后修改时间
 */
@TableField(fill = FieldFill.INSERT_UPDATE)
private LocalDateTime gmtModified;
```

# 新增类别 -- Repository 

关于Repository的作用：再议

Repository和Mapper同属于DAO层的persist之下，所以，在项目中分包时，大多会分为：

```
-- dao
-- -- persist
-- -- -- mapper
-- -- -- repository
```

在实际编写时，需要自行创建Repository的接口及实现类，此层仍是解决数据访问的层。

则在项目的根包下创建`dao.persist.repository.ICategoryRepository`接口，并在接口中添加抽象方法：

```java
public interface ICategoryRepository {
    int insert(Category category);
}
```

并且，在`dao.persist.repository.impl.CategoryRepositoryImpl`类，实现以上接口，在类上添加`@Repository`注解，在类中自动装配Mapper对象，并通过Mapper对象来实现重写的方法的功能，例如：

```java
@Repository
public class CategoryRepositoryImpl implements ICategoryRepository {
    
    @Autowired
    private CategoryMapper categoryMapper;
    
    @Override
    public int insert(Category category) {
        return categoryMapper.insert(category);
    }
    
}
```

# 新增类别 -- Service

Service层也是由接口和实现类共同完成的，也都是自行开发的！

Service的作用是：设计业务流程与业务逻辑，以保证数据的完整性、有效性、安全性。

关于Service中的方法的声明原则：

- 返回值类型：仅以操作成功为前提进行设计
  - 如果操作失败，将通过抛出异常的方式来表示

- 方法名称：自定义
- 参数列表：取决于客户端提交的请求参数，另外，还可能包括Controller能获取的数据，例如当事人信息

关于方法的返回值设计的示例：

```java
User login(String username, String password) throws 用户名不存在异常, 密码错误异常, 账号被禁用的异常;
```

```java
try {
    User user = service.login("root", "123456");
	System.out.println("登录成功，用户信息：" + user); 
} catch (用户名不存在异常 e) {
    System.out.println("登录失败，用户名不存在"); 
} catch (密码错误异常 e) {
    System.out.println("登录失败，密码错误"); 
} catch (账号被禁用的异常 e) {
    System.out.println("登录失败，账号被禁用"); 
}
```

在项目的根包下创建`pojo.param.CategoryAddNewParam`类，作为客户端提交的请求参数类型：

```java
@Data
public class CategoryAddNewParam implements Serializable {

    /**
     * 类别名称
     */
    private String name;

    /**
     * 父级类别ID，如果无父级，则为0
     */
    private Long parentId;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    private String keywords;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 图标图片的URL
     */
    private String icon;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;
 
}
```

在项目的根包下创建`service.ICategoryService`接口，并在接口中添加抽象方法：

```java
public interface ICategoryService {
    // 新增类别
    void addNew(CategoryAddNewParam categoryAddNewParam);
}
```

在项目的根包下创建`service.impl.CategoryServiceImpl`类，实现以上接口，并在类上添加`@Service`注解：

```java
@Service
public class CategoryServiceImpl implements ICategoryService {
    
}
```















# 新增类别 -- Service 
# 新增类别 -- Controller 





