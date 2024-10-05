# 删除类别 -- Mapper 

（略）

# 删除类别 -- Repository 

（略，此前的作业）

# 删除类别 -- Service 

注意：此前的作业完成了基本的删除功能，但是，未实现业务逻辑！

当尝试删除某个类别时，需要设计的业务逻辑可能包括：

- 如果数据不存在（查询），则不执行删除
- 如果是父级类别（isParent值为1），则不允许删除
- 如果有商品关联到此类别，则不允许删除
- 如果这是父级中的最后一个子级（基于父级的ID执行统计，且删除当前后统计结果为0），则将父级的isParent更新为0

实现代码为：

```java
@Override
public void delete(Long id) {
    log.debug("开始处理【根据ID删除类别】的业务，参数：{}", id);
    CategoryStandardVO queryResult = categoryRepository.getStandardById(id);
    if (queryResult == null) {
        String message = "删除类别失败，尝试访问的数据不存在！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
    }

    if (queryResult.getIsParent().equals(1)) {
        String message = "删除类别失败，此类别还包含了子级类别！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
    }

    int countByCategory = goodsRepository.countByCategory(id);
    if (countByCategory > 0) {
        String message = "删除类别失败，仍有商品关联到了此类别！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
    }

    int rows = categoryRepository.deleteById(id);
    if (rows != 1) {
        String message = "删除类别失败，服务器忙，请稍后再尝试！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_DELETE, message);
    }

    Long parentId = queryResult.getParentId();
    int countByParent = categoryRepository.countByParent(parentId);
    if (countByParent == 0) {
        Category updateCategory = new Category();
        updateCategory.setId(parentId);
        updateCategory.setIsParent(0);
        rows = categoryRepository.update(updateCategory);
        if (rows != 1) {
            String message = "删除类别失败，服务器忙，请稍后再尝试！";
            log.warn(message);
            throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
        }
    }
}
```

# 删除类别 -- Controller 

（略，此前的作业）

# 删除类别 -- 页面

（略）

# 启用和禁用 -- Mapper 

（略，直接使用MyBatis Plus的修改数据功能即可）

# 启用和禁用 -- Repository 

（略，已完成）

# 启用和禁用 -- Service 

先在`ICategoryService`中添加抽象方法：

```java
int ENABLE_STATE_OFF = 0;
int ENABLE_STATE_ON = 1;
String[] ENABLE_STATE_TEXT = {"禁用", "启用"};

void setEnable(Long id); // enable > 1
void setDisable(Long id); // enable > 0
```

再在`CategoryServiceImpl`中实现以上方法：

```java
@Override
public void setEnable(Long id) {
    updateEnableById(id, ENABLE_STATE_ON);
}

@Override
public void setDisable(Long id) {
    updateEnableById(id, ENABLE_STATE_OFF);
}

private void updateEnableById(Long id, Integer enable) {
    CategoryStandardVO queryResult = categoryRepository.getStandardById(id);
    if (queryResult == null) {
        String message = ENABLE_STATE_TEXT[enable] + "类别失败，尝试访问的数据不存在！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
    }

    if (queryResult.getEnable().equals(enable)) {
        String message = ENABLE_STATE_TEXT[enable] + "类别失败，此类别已经是【" + ENABLE_STATE_TEXT[enable] + "】状态！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
    }

    Category updateCategory = new Category();
    updateCategory.setId(id);
    updateCategory.setEnable(enable);
    int rows = categoryRepository.update(updateCategory);
    if (rows != 1) {
        String message = ENABLE_STATE_TEXT[enable] + "类别失败，服务器忙，请稍后再试！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
    }
}
```

# 启用和禁用 -- Controller 

```java
@PostMapping("/{id:[0-9]+}/enable")
@ApiOperation("启用类别")
@ApiOperationSupport(order = 310)
@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
})
public JsonResult setEnable(@Range(min = 1, message = "请提交合法的ID值")
                            @PathVariable Long id) {
    log.debug("开始处理【启用类别】的请求，参数：{}", id);
    categoryService.setEnable(id);
    return JsonResult.ok();
}

@PostMapping("/{id:[0-9]+}/disable")
@ApiOperation("禁用类别")
@ApiOperationSupport(order = 311)
@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
})
public JsonResult setDisable(@Range(min = 1, message = "请提交合法的ID值")
                             @PathVariable Long id) {
    log.debug("开始处理【禁用类别】的请求，参数：{}", id);
    categoryService.setDisable(id);
    return JsonResult.ok();
}
```

# 启用和禁用 -- 页面

（略）

# 根据ID查询类别 -- Mapper 

（略）

# 根据ID查询类别 -- Repository 

（略，此前的作业）

# 根据ID查询类别 -- Service 

（略，此前的作业）

# 根据ID查询类别 -- Controller 

（略，此前的作业）

# 根据ID查询类别 -- 页面

（略）

# 修改类别 -- Mapper 

（略）

# 修改类别 -- Repository 

（略）

# 修改类别 -- Service 

在项目的根包下创建`pojo.param.CategoryUpdateInfoParam`类，在类中封装新数据对应的属性：

```java
@Data
public class CategoryUpdateInfoParam  implements Serializable {

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
     * 图标图片的URL
     */
    private String icon;

}
```

在`ICategoryService`中添加抽象方法：

```java
void updateInfoById(Long id, CategoryUpdateInfoParam categoryUpdateInfoParam);
```

在`CategoryServiceImpl`中实现以上方法：

```java
@Override
public void updateInfoById(Long id, CategoryUpdateInfoParam categoryUpdateInfoParam) {
    CategoryStandardVO queryResult = categoryRepository.getStandardById(id);
    if (queryResult == null) {
        String message = "修改类别失败，尝试访问的数据不存在！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_NOT_FOUND, message);
    }

    String name = categoryUpdateInfoParam.getName();
    int count = categoryRepository.countByNameAndNotId(id, name);
    if (count > 0) {
        String message = "修改类别失败，类别名称已经被占用！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
    }

    Category updateCategory = new Category();
    BeanUtils.copyProperties(categoryUpdateInfoParam, updateCategory);
    updateCategory.setId(id);
    int rows = categoryRepository.update(updateCategory);
    if (rows != 1) {
        String message = "修改类别失败，服务器忙，请稍后再试！";
        log.warn(message);
        throw new ServiceException(ServiceCode.ERROR_UPDATE, message);
    }
}
```

# 修改类别 -- Controller 

```java
@PostMapping("/{id:[0-9]+}/update")
@ApiOperation("修改类别详情")
@ApiOperationSupport(order = 300)
@ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "类别ID", required = true, dataType = "long")
})
public JsonResult setDisable(@PathVariable @Range(min = 1, message = "请提交合法的ID值") Long id,
                             @Valid CategoryUpdateInfoParam categoryUpdateInfoParam) {
    log.debug("开始处理【修改类别详情】的请求，ID：{}，新数据：{}", id, categoryUpdateInfoParam);
    categoryService.updateInfoById(id, categoryUpdateInfoParam);
    return JsonResult.ok();
}
```

# 修改类别 -- 页面