# 基于Spring Boot的Elasticsearch编程

## 添加依赖

在Spring Boot中，实现Elasticsearch编程需要添加依赖：`spring-boot-starter-data-elasticsearch`

## 定义文档数据的Java类

注意：与ES中的文档对应的Java类，并不需要与实体类、用于Redis的相关类保持一致！

使用ES时的Java类可以完全自由设计，只需要包含显示在“搜索结果中的属性（最终显示在客户端界面中的属性）”及“执行搜索时需要匹配的属性”即可。

```java
@Data
public class GoodsSearchVO implements Serializable {
	private Long id;
    private String categoryName;
    private String title;
    private String brief;
    private String coverUrl;
    private BigDecimal salePrice;
    private String keywords;
    private Integer sort;
    private Integer isRecommend;
    private Integer salesCount;
    private Integer commentCount;
    private Integer positiveCommentCount;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
}
```











## 编写数据访问接口

在Spring Boot项目中添加了Elasticsearch编程的依赖项后，只需要自定义接口，继承自框架中的`Repository`接口即可表示自定义接口是用于访问数据的，`Repository`接口需要指定2个泛型，分别是你要操作的数据的类型，和此数据在ES中的ID的类型。











