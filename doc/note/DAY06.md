# 基于Spring JDBC的事务管理

事务：是数据库中的能够保证多个写操作（增删改）要么全部成功，要么全部失败的一种机制！

假设存在需求：国斌向传奇转账500元，需要执行的SQL操作可能是：

```mysql
update 账户余额表 set 余额=余额+500 where 账户='传奇';
```

```mysql
update 账户余额表 set 余额=余额-500 where 账户='国斌';
```

以上2个SQL操作，如果全部成功，是达成预期的，如果全部失败，数据没有出错，也是可以接受的，如果只成功一半（第1个成功，第2个失败），则数据不一致，是不可接受的！

在基于Spring JDBC的项目中，只需要在方法上添加`@Transactional`注解，即可使得此方法是“事务性”的（具有要么全部成功，要么全部失败的特点）。

关于`@Transactional`注解，可以：

- 添加在接口上：将作用于接口中所有抽象方法，也就作用于所有重写的方法
- 添加在（接口中的）抽象方法上：将作用于重写的方法
- 添加在类上：将作用于此类中所有的方法，注意：只能作用于重写的接口中的方法
- 添加在（类中的）方法上：将作用于此方法，注意：只能作用于重写的接口中的方法

在学习过程中，建议在接口上添加`@Transactional`，一来比较省事，二来避免需求变化后忘记补充注解（相对添加在方法上）。

事务的本质是关闭了数据库的“自动提交”的机制，当关闭后，即使SQL语句执行成功，也不会同步到硬盘的数据库文件上，后续，如果发现执行出错，则完全不提交，如果全部执行成功，再统一提交。

Spring JDBC的事务管理机制大致如下：

```
try {
	开启事务（关闭自动提交）：BEGIN
	执行你的业务方法
	提交事务：COMMIT
} catch (RuntimeException e) {
	回滚事务：ROLLBACK
} finally {
	打开自动提交
}
```

提示：Spring JDBC在调用原生JDBC技术时，捕获了`SQLException`，并且，抛出了自定义的相关异常，这些异常都是`RuntimeException`的子孙类异常！

所以，在默认情况下，`@Transactional`只会根据`RuntimeException`执行回滚，如果需要按照其它异常回滚，可以配置`@Transactional`注解中的`rollbackFor`属性或`rollbackForClassName`，例如：

```java
@Transactional(rollbackFor = {NullPointerException.class, ClassCastException.class})
```

```java
@Transactional(rollbackForClassName = {"java.lang.NullPointerException", "java.lang.ClassCastException"})
```

也可以指定哪些异常不会导致回滚，需要配置`noRollbackFor`或`noRollbackForClassName`属性。

小结：

- 从学术的角度，如果某个业务方法涉及1次以上的写操作，则必须保证此业务方法是事务性的，从目前学习的角度，直接使得所有业务方法都是事务性的，所以，在业务接口上添加`@Transactional`注解
- 在业务方法的实现中，每次执行了增、删、改操作后，都应该获取受影响的行数，并且，判断此值是否符合预期，如果不符合，则抛出`RuntimeException`或其子孙类异常，使得事务回滚

关于事务，你还应该补充：事务的四大特性（ACID特性）、事务的传播、事务的隔离。

# 新增类别 -- Controller 

需要在项目中添加`spring-boot-starter-web`依赖项！

提示：`spring-boot-starter-???`都包含了`spring-boot-starter`，所以，调整依赖项时，只需要将原本的`spring-boot-starter`改为`spring-boot-starter-web`即可。

创建控制器，简单的处理请求，大致如下：

```java
package cn.tedu.tmall.admin.mall.controller;

import cn.tedu.tmall.admin.mall.pojo.param.CategoryAddNewParam;
import cn.tedu.tmall.admin.mall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    // http://localhost:8080/categories/add-new?name=ShuiGuo&parentId=0
    @RequestMapping("/add-new")
    public void addNew(CategoryAddNewParam categoryAddNewParam) {
        categoryService.addNew(categoryAddNewParam);
    }

}
```

## 添加Knife4j

在基于Spring Boot的项目中，使用Knife4j至少需要：

- 添加依赖
- 编写配置类
- 在配置文件中配置：`knife4j.enable=true`

关于依赖项（2.0.9适用于Spring Boot 2.5.x版本）：

```xml
<knife4j-spring-boot.version>2.0.9</knife4j-spring-boot.version>
```

```xml
<!-- Knife4j Spring Boot：在线API文档 -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
    <version>${knife4j-spring-boot.version}</version>
</dependency>
```

关于配置类（可能需要调整`BASE_PACKAGE`属性的值）：

```java
package cn.tedu.tmall.admin.mall.config;


import com.github.xiaoymin.knife4j.spring.extension.OpenApiExtensionResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * Knife4j配置类
 *
 * @author YiRunDong
 * @version 2.0
 */
@Slf4j
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    /**
     * 【重要】指定Controller包路径
     */
    private static final String BASE_PACKAGE = "cn.tedu.tmall";
    /**
     * 组名
     */
    private static final String GROUP_NAME = "学茶商城";
    /**
     * 主机名
     */
    private static final String HOST = "http://java.tedu.cn";
    /**
     * 标题
     */
    private static final String TITLE = "学茶商城-商城管理后台服务-在线API文档";
    /**
     * 简介
     */
    private static final String DESCRIPTION = "学茶商城-商城管理后台服务-在线API文档";
    /**
     * 服务条款URL
     */
    private static final String TERMS_OF_SERVICE_URL = "http://www.apache.org/licenses/LICENSE-2.0";
    /**
     * 联系人
     */
    private static final String CONTACT_NAME = "Java教学研发部";
    /**
     * 联系网址
     */
    private static final String CONTACT_URL = "http://java.tedu.cn";
    /**
     * 联系邮箱
     */
    private static final String CONTACT_EMAIL = "java@tedu.cn";
    /**
     * 版本号
     */
    private static final String VERSION = "2.0";

    @Autowired
    private OpenApiExtensionResolver openApiExtensionResolver;

    public Knife4jConfiguration() {
        log.debug("创建配置类对象：Knife4jConfiguration");
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .host(HOST)
                .apiInfo(apiInfo())
                .groupName(GROUP_NAME)
                .select()
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .extensions(openApiExtensionResolver.buildExtensions(GROUP_NAME));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(TITLE)
                .description(DESCRIPTION)
                .termsOfServiceUrl(TERMS_OF_SERVICE_URL)
                .contact(new Contact(CONTACT_NAME, CONTACT_URL, CONTACT_EMAIL))
                .version(VERSION)
                .build();
    }

}
```

并在`application.yaml`中添加配置：

```yaml
knife4j:
  enable: true
```

完成后，重启项目，通过 http://localhost:8080/doc.html 测试访问

# 关于控制器层的响应结果

无论是处理请求时正确的响应，还是后续处理异常后的响应，目前主流的做法是向客户端响应JSON格式的结果，则需要自定义某个数据类型（严格来说，是Spring MVC没有默认的消息转换器的类型），则Spring MVC框架会自动使用Jackson框架中的消息转换器，将方法（处理请求的方法，或处理异常的方法）的返回值转换为JSON结果！

首先，应该创建一个枚举类型，用于穷举所有的业务状态码，例如：

```java
/**
 * 业务状态码
 *
 * @author YiRunDong
 * @version 2.0
 */
public enum ServiceCode {

    /**
     * 操作成功
     */
    OK(20000),
    /**
     * 错误：请求参数格式错误
     */
    ERROR_BAD_REQUEST(40000),
    /**
     * 错误：未认证
     */
    ERROR_UNAUTHORIZED(40100),
    /**
     * 错误：未认证，因为被禁用
     */
    ERROR_UNAUTHORIZED_DISABLED(40101),
    /**
     * 错误：禁止访问，用于无权限
     */
    ERROR_FORBIDDEN(40300),
    /**
     * 错误：数据不存在
     */
    ERROR_NOT_FOUND(40400),
    /**
     * 错误：数据冲突
     */
    ERROR_CONFLICT(40900),
    /**
     * 错误：未知的插入数据失败
     */
    ERROR_INSERT(50000),
    /**
     * 错误：未知的删除数据失败
     */
    ERROR_DELETE(50100),
    /**
     * 错误：未知的修改数据失败
     */
    ERROR_UPDATE(50200),
    /**
     * 错误：JWT已过期
     */
    ERR_JWT_EXPIRED(60000),
    /**
     * 错误：JWT验证签名失败，可能使用了伪造的JWT
     */
    ERR_JWT_SIGNATURE(60100),
    /**
     * 错误：JWT格式错误
     */
    ERR_JWT_MALFORMED(60200),
    /**
     * 错误：上传的文件为空（没有选择有效的文件）
     */
    ERROR_UPLOAD_EMPTY(90000),
    /**
     * 错误：上传的文件类型有误
     */
    ERROR_UPLOAD_INVALID_TYPE(90100),
    /**
     * 错误：上传的文件超出限制
     */
    ERROR_UPLOAD_EXCEED_MAX_SIZE(90200),
    /**
     * 错误：其它异常
     */
    ERROR_UNKNOWN(99999);

    /**
     * 枚举对象的值
     */
    private Integer value;

    ServiceCode(Integer value) {
        this.value = value;
    }

    /**
     * 获取枚举对象的值
     *
     * @return 枚举对象的值
     */
    public Integer getValue() {
        return value;
    }

}
```

然后，创建新的类，作为响应结果的数据类型：

```java
/**
 * 服务器端的统一响应类型
 *
 * @author YiRunDong
 * @version 2.0
 */
@Data
public class JsonResult {
    
    /**
     * 操作结果的状态码（状态标识）
     */
    @ApiModelProperty("业务状态码")
    private Integer state;
    
    /**
     * 操作失败时的提示文本
     */
    @ApiModelProperty("提示文本")
    private String message;
    
    /**
     * 操作成功时响应的数据
     */
    @ApiModelProperty("数据")
    private Object data;

    /**
     * 生成表示"成功"的响应对象
     *
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok() {
        return ok(null);
    }

    /**
     * 生成表示"成功"的响应对象，此对象中将包含响应到客户端的数据
     *
     * @param data 响应到客户端的数据
     * @return 表示"成功"的响应对象
     */
    public static JsonResult ok(Object data) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = ServiceCode.OK.getValue();
        jsonResult.data = data;
        return jsonResult;
    }

    /**
     * 生成表示"失败"的响应对象
     *
     * @param serviceCode 业务状态码
     * @param message     提示文本
     * @return 表示"失败"的响应对象
     */
    public static JsonResult fail(ServiceCode serviceCode, String message) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.state = serviceCode.getValue();
        jsonResult.message = message;
        return jsonResult;
    }

}
```

完成后，调整控制器中处理请求的方法的返回结果：

```java
@PostMapping("/add-new")
public JsonResult addNew(CategoryAddNewParam categoryAddNewParam) {
    categoryService.addNew(categoryAddNewParam);
    return JsonResult.ok();
}
```

# 关于响应结果中的null值

当前项目中，响应的JSON数据中会包含`JsonResult`类中所有的属性，但是，每个请求都必然至少有1个属性的值为`null`，这些为`null`的属性没有必要出现在响应结果的JSON数据中！

可以通过`@JsonInclude`注解进行配置，例如：

```java
@JsonInclude(JsonInclude.Include.NON_NULL)
```

当把注解添加在属性上，则作用于当前属性；当把注解添加在类上，则作用于当前类中所有属性！

还可以在配置文件中配置`spring.jackson.default-property-inclusion`属性，使得当前项目中所有响应的类型（包括作为`JsonResult`对象的`data`属性的数据类型）都应用此配置，例如：

```properties
spring.jackson.default-property-inclusion=non_null
```

```yaml
spring:
  jackson:
    default-property-inclusion: non_null
```

# 使用全局异常处理器统一处理异常

首先，在Service中抛出的异常不应该是某个已知的异常，因为这些异常都有其对应的错误，例如`NullPointerException`，如果在Service中自行抛出NPE，后续，当捕获到NPE时，将无法区分到底是“真的因为空指针导致的错误”还是“不符合业务规则的错误”，所以，应该自定义异常！

自定义异常应该继承自`RuntimeException`，因为：

- 配合Spring JDBC的事务管理机制，在Service方法中抛出自定义异常时，可以回滚
- 当前项目会使用到全局异常处理器来处理各种异常，而全局异常处理器只能处理Controller抛出的异常，而Controller会调用Service，Service必须抛出异常，那么，如果自定义不是继承自`RuntimeException`，则Service接口、Service实现类、Controller中所有的方法都必须显式的通过`throws`关键字声明抛出异常，由于Service、Controller抛出异常是固定的做法，没有必要都显式的声明抛出，所以，使用`RuntimeException`会更加便利

并且，在抛出异常时，应该区分出是因为什么原因导致的异常，后续处理异常时才能给出正确的业务状态码（例如40400、40900等），可选的解决方案有：

- 创建多个异常类型，在不同的错误时，抛出不同的异常
- 在自定义异常中添加属性，通过属性区分不同的错误，每次抛出异常时必须确定此属性的值

综合看来，创建的异常类应该是：

```java
public class ServiceException extends RuntimeException {

    private ServiceCode serviceCode;

    public ServiceException(ServiceCode serviceCode, String message) {
        super(message);
        this.serviceCode = serviceCode;
    }

    public ServiceCode getServiceCode() {
        return serviceCode;
    }

}
```

然后，在`JsonResult`中添加新的方法：

```java
/**
 * 生成表示"失败"的响应对象
 *
 * @param e 业务异常
 * @return 表示"失败"的响应对象
 */
public static JsonResult fail(ServiceException e) {
    return fail(e.getServiceCode(), e.getMessage());
}
```

并且，使用全局异常处理器处理`ServiceException`：

```java
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public JsonResult handleServiceException(ServiceException e) {
        log.warn("由全局异常处理器开始处理ServiceException");
        return JsonResult.fail(e);
    }

}
```

需要注意，在全局异常处理器中，你应该添加一个方法，用于处理`Throwable`类型的异常！

```java
@ExceptionHandler
public JsonResult handleThrowable(Throwable e) {
    log.warn("由全局异常处理器开始处理Throwable");
    log.warn("", e);
    String message = "服务器忙，请稍后再试！【同学们，当你们看到这个提示时，请检查服务器端的控制台，发现异常，并在全局异常处理器中补充处理此异常的方法】";
    return JsonResult.fail(ServiceCode.ERROR_UNKNOWN, message);
}
```











