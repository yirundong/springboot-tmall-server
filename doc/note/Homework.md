# 第1次作业（2023.07.25）

在`tmall-admin-mall`项目中，按需创建Mapper接口及对应的XML文件、Repository接口与实现类，保证各Repository实现以下功能：

- `ICategoryRepository`
  - 根据id删除类别数据：`where id=?`
  - 统计当前表中非此类别id的匹配名称的类别数据的数量：`where name=? and id<>?`
  - 根据父级类别，统计其子级类别的数量：`where parentId=?`
- `IGoodsRepository`
  - 插入商品数据
  - 根据ID删除商品数据
  - 根据id修改商品数据
  - 根据类别统计商品数量：`where category_id=?`
  - 根据id查询商品数据详情
- `IGoodsDetailRepository`
  - 插入商品详情数据
  - 根据商品ID删除商品详情数据：`where goods_id=?`
  - 根据商品ID修改商品详情数据：`where goods_id=?`
- `ICommentRepository`
  - 根据商品ID删除评论：`where goods_id=?`
  - 根据ID修改评论数据

以上功能完成后，均需编写对应的测试，以保证所有功能的执行效果符合预期。

作业提交截止时间：明天（07.26）早上09:00

# 第2次作业（2023.07.26）

**今晚有课（简历制作），减少作业量**

在`tmall-admin-mall`项目中，实现功能：

- 根据ID删除类别
  - Controller中配置的路径应该是：`/{id:[0-9]+}/delete`
- 根据ID查询类别
  - Controller中配置的路径应该是：`/{id:[0-9]+}`
- 新增商品
  - 新增商品的参数可自行分析

以上作业项的要求：

- 要求完成Service / Controller中的代码
- 在编写Service时，均不需要考虑任何业务规则（即：在Service中调用Repository完成核心功能即可）
- 需要编写对应的测试类与测试方法
- 需要通过API文档测试访问

作业提交截止时间：明天（07.27）早上09:00





# 第4次作业（2023.07.29）

- 在老师的项目中找到`/doc/sql/create_table_content.sql`，在当前数据库中，创建资讯相关的数据表
- 在自己的后端项目中的`tmall-admin`下，创建新的子模块项目`tmall-admin-content`
- 在`tmall-admin-content`中，实现“添加类别”功能，直至可以通过API文档测试方法
- 在自己的前端项目中，实现“添加类别”的页面与功能

作业提交截止时间：下周二（08.01）早上09:00

附：实体类代码：

```java
@Data
@TableName("content_category")
public class Category implements Serializable {

    /**
     * 数据ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 类别名称
     */
    private String name;

    /**
     * 关键词列表，各关键词使用英文的逗号分隔
     */
    private String keywords;

    /**
     * 排序序号
     */
    private Integer sort;

    /**
     * 是否启用，1=启用，0=未启用
     */
    private Integer enable;

    /**
     * 是否显示在导航栏中，1=启用，0=未启用
     */
    private Integer isDisplay;

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

}
```

# 第5次作业（2023.07.31）

在后端和前端项目中完成：查看资讯的类别列表

作业提交截止时间：3天后（08.03）早上09:00

# 第6次作业（2023.08.01）

在后端和前端项目中完成：切换类别的显示状态（显示与不显示）

作业提交截止时间：明天（08.02）早上09:00

# 第7次作业（2023.08.05）

参考老师的项目（发布作业时已经同步更新），在tmall-admin-content项目中完成文章管理相关功能，包括：

- 发布文章
- 根据ID删除文章
- 显示文章
- 隐藏文章
- 根据ID查询文章
- 查询文章列表
- 根据类别查询文章列表

并在根项目下创建tmall-attachment项目，用于处理文件上传并显示上传的静态资源，包括：

- 上传用户头像
- 上传文章图片
- 上传商品图片

作业提交截止时间：下周二（08.08）早上09:00