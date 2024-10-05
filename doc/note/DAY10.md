# 自定义分页结果类型

使用PageHelper实现分页时，结果通常使用`PageInfo`类型，但是，此类型中包含的相关数据太多，可能是没有必要的，例如：

```
prePage=0, 
nextPage=2, 
isFirstPage=true, 
isLastPage=false, 
hasPreviousPage=false, 
hasNextPage=true, 
navigatePages=8, 
navigateFirstPage=1, 
navigateLastPage=4, 
navigatepageNums=[1, 2, 3, 4]
```

以上数据基本上都是不必要的！

为了使得响应到客户端的数据是简单、有效的，可以自行定义数据类型，作为分页的数据结果，例如：

```java
@Data
@Accessors(chain = true)
public class PageData<T> implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer maxPage;
    private List<T> list;

}
```

由于将`PageInfo`转变为以上`PageData`类型的代码是相对固定的，不必要在每次分页查询时都写相同的代码进行转换，则可以：

- 在`PageData`类上添加使用`PageInfo`参数的构造方法，在构造方法内部封装必要的数据即可
- 使用新的工具类，在此工具类提供“将`PageInfo`转换为`PageData`的方法”

以使用工具类为例：

```java
public class PageInfoToPageDataConverter {

    public static <T> PageData<T> convert(PageInfo<T> pageInfo) {
        PageData<T> pageData = new PageData<>();
        return pageData.setList(pageInfo.getList())
                .setPageNum(pageInfo.getPageNum())
                .setPageSize(pageInfo.getPageSize())
                .setTotal(pageInfo.getTotal())
                .setMaxPage(pageInfo.getPages());
    }

}
```

# 根据父级查询子级类别列表--Repository层

在`ICategoryRepository`中添加抽象方法：

```java
/**
 * 根据父级类别查询其子级类别列表
 *
 * @param parentId 父级类别的ID
 * @param pageNum  页码
 * @param pageSize 每页数据量
 * @return 类别列表
 */
PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum, Integer pageSize);
```

并在`CategoryRepositoryImpl`中实现：

```java
@Override
public PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum, Integer pageSize) {
    log.debug("开始执行【根据父级类别查询其子级类别列表】的数据访问，父级类别：{}，页码：{}，每页数据量：{}", parentId, pageNum, pageSize);
    PageHelper.startPage(pageNum, pageSize);
    List<CategoryListItemVO> list = categoryMapper.listByParent(parentId);
    PageInfo<CategoryListItemVO> pageInfo = new PageInfo<>(list);
    // PageData<CategoryListItemVO> pageData = PageInfoToPageDataConverter.convert(pageInfo);
    // return pageData;
    // 【非常不推荐】return PageInfoToPageDataConverter.convert(new PageInfo<>(categoryMapper.listByParent(parentId)));
    return PageInfoToPageDataConverter.convert(pageInfo);
}
```

完成后，编写并执行测试：

```java
@Test
void listByParent() {
    Long parentId = 0L;
    Integer pageNum = 1;
    Integer pageSize = 8;
    PageData<?> pageData = repository.listByParent(parentId, pageNum, pageSize);
    System.out.println("页码：" + pageData.getPageNum());
    System.out.println("每页数据量：" + pageData.getPageSize());
    System.out.println("总数据量：" + pageData.getTotal());
    System.out.println("总页数：" + pageData.getMaxPage());
    System.out.println("列表：" + pageData.getList());
}
```

# 根据父级查询子级类别列表--Service层

先在`ICategoryService`中声明抽象方法：

```java
/**
 * 根据父级类别查询其子级类别列表，每页的数据量将使用默认值
 *
 * @param parentId 父级类别的ID
 * @param pageNum  页码
 * @return 类别列表
 */
PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum);

/**
 * 根据父级类别查询其子级类别列表
 *
 * @param parentId 父级类别的ID
 * @param pageNum  页码
 * @param pageSize 每页数据量
 * @return 类别列表
 */
PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum, Integer pageSize);
```

并在`CategoryServiceImpl`中实现以上方法：

```java
@Override
public PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum) {
    return categoryRepository.listByParent(parentId, pageNum, 20);
}

@Override
public PageData<CategoryListItemVO> listByParent(Long parentId, Integer pageNum, Integer pageSize) {
    return categoryRepository.listByParent(parentId, pageNum, pageSize);
}
```

# 根据父级查询子级类别列表--Controller层

在`CategoryController`中处理请求：

```java
@GetMapping("/list-by-parent")
@ApiOperation("根据父级查询子级列表")
@ApiOperationSupport(order = 420)
@ApiImplicitParams({
        @ApiImplicitParam(name = "parentId", value = "父级类别ID", required = true, paramType = "query", dataType = "long"),
        @ApiImplicitParam(name = "page", value = "页码", paramType = "query", defaultValue = "1", dataType = "int"),
        @ApiImplicitParam(name = "queryType", value = "查询类型", paramType = "query", example = "all")
})
public JsonResult listByParent(@Range(message = "请提交有效的父级类别ID值！") Long parentId,
                               @Range(min = 1, message = "请提交有效的页码值！") Integer page,
                               String queryType) {
    Integer pageNum = page == null ? 1 : page;
    PageData<CategoryListItemVO> pageData;
    if ("all".equals(queryType)) {
        pageData = categoryService.listByParent(parentId, pageNum, Integer.MAX_VALUE);
    } else {
        pageData = categoryService.listByParent(parentId, pageNum);
    }
    return JsonResult.ok(pageData);
}
```









